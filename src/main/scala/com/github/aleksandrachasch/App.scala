package com.github.aleksandrachasch

import com.github.aleksandrachasch.AvroTools.schema
import com.github.aleksandrachasch.avro.Customer
import com.github.aleksandrachasch.avro.ops.DataProcessor.DataProcessorOps
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.functions.{col, explode}

object App extends SparkSessionWrapper {

  def main(args : Array[String]) = {

    import spark.implicits._
    import com.github.aleksandrachasch.avro.ops.CustomerDataProcessor._

    val ds = spark.read.format("json")
      .schema(ScalaReflection.schemaFor[Customer].dataType.asInstanceOf[StructType])
      .option("mode", "FAILFAST")
      .option("multiline", true)
      .load("src/main/resources/customer-data.json")
      .as[Customer]

    ds.show(false)

    val flattenedDs = ds.flatMap(_.customFlatten)
    flattenedDs.show
  }

}
