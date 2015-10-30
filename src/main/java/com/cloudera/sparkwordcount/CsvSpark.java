package com.cloudera.sparkwordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import scala.Tuple2;

import com.mongodb.hadoop.MongoOutputFormat;

public class CsvSpark {

	public static void main(String[] args) {

		JavaSparkContext sc = new JavaSparkContext("local", "Java Word Count");

		SQLContext sqlContext = new SQLContext(sc);
		DataFrame df = sqlContext.read().format("com.databricks.spark.csv").option("header", "true").load("cars.csv");
		DataFrame a = df.select("year", "model","comment","quantity");//write().format("com.databricks.spark.csv").save("newcars.csv");

		DataFrame b = a.select(df.col("quantity").multiply(2));
		b.show();



		JavaRDD<String> data = sc.textFile("/home/cloudera/workspace/simplesparkapp/cars.csv");



		Function<String, Boolean> filter = new Function<String, Boolean>() {
			@Override
			public Boolean call(String arg0) throws Exception {
				return !arg0.startsWith("year");
			}
		};


		JavaRDD<Cars> rdd_records = data.filter(filter).map(
				new Function<String, Cars>() {
					public Cars call(String line) throws Exception {
						String[] fields = line.split(",");
						Cars sd = new Cars(fields[0], fields[1], fields[2].trim(), fields[3],Integer.parseInt(fields[4]));
						return sd;
					}
				});

		VoidFunction<Cars> printFunc = new VoidFunction<Cars>() {
			@Override
			public void call(Cars arg0) throws Exception {
				System.out.println(arg0.toString());
			}
		};
		rdd_records.foreach(printFunc);


		PairFunction<Cars, Object, BSONObject> f = new PairFunction<Cars, Object, BSONObject>() {
			@Override
			public Tuple2<Object, BSONObject> call(Cars arg0) throws Exception {
				return new Tuple2(null, arg0.convertToBson());
			}
		};		

		// Output contains tuples of (null, BSONObject) - ObjectId will be generated by Mongo driver if null
		JavaPairRDD<Object, BSONObject> rdd_recordsBson = rdd_records.mapToPair(f);


		Configuration config = new Configuration();
		config.set("mongo.input.uri", "mongodb://127.0.0.1:27017/beowulf.input");
		config.set("mongo.output.uri", "mongodb://127.0.0.1:27017/beowulf.cars");

		// Only MongoOutputFormat and config are relevant
		rdd_recordsBson.saveAsNewAPIHadoopFile("file:///bgus", Object.class, Object.class, MongoOutputFormat.class, config);


	}

}