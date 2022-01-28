package com.github.alchash

import com.github.alchash.AvroTools.schema
import com.github.alchash.avro.Customer
import com.github.alchash.avro.ops.DataProcessor.DataProcessorOps
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

object App extends SparkSessionWrapper {

  def main(args : Array[String]) = {

    import spark.implicits._

    import com.github.alchash.avro.ops.CustomerDataProcessor._

    println(schema.toString(true))

    val ds = spark.read.format("json")
      .schema(ScalaReflection.schemaFor[Customer].dataType.asInstanceOf[StructType])
      .option("mode", "FAILFAST")
      .option("multiline", true)
      .load("src/main/resources/customer-data.json")
      .as[Customer]

    ds.show(false)

    ds.head.customFlatten.toDS().show(false)
  }

}
