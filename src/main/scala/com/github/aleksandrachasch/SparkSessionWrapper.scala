package com.github.aleksandrachasch

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper {

  lazy val spark = SparkSession.builder()
    .appName("SparkAvroApp")
    .master("local")
    .getOrCreate

}
