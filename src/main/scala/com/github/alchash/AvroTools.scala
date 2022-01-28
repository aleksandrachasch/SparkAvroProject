package com.github.alchash

import org.apache.avro.Schema.Parser

import java.io.File


object AvroTools {

  val schema = new Parser().parse( new File("src/main/resources/schema.avsc"))

}
