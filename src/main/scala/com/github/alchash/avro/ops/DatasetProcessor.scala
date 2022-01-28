package com.github.alchash.avro.ops

import com.github.alchash.avro.{Customer, ops}
import org.apache.spark.sql.Dataset

trait DataProcessor[T, C] {
  def customFlatten(t: T): Seq[C]
}

object DataProcessor {

  def apply[T, C](implicit processor: DataProcessor[T, C]): DataProcessor[T, C] =
    processor

  def instance[T, C](fun: T => Array[C]): DataProcessor[T, C] =
    (t: T) => fun(t)

  implicit class DataProcessorOps[T, C](val t : T) extends AnyVal {
    def customFlatten(implicit processor: DataProcessor[T, C]): Seq[C] = {
      processor.customFlatten(t)
    }
  }

}

object CustomerDataProcessor {

  implicit val customerDataProcessor: DataProcessor[Customer, FlattenedCustomer] =
    (t: Customer) => t.Accounts.flatMap(acc => acc.InteractionGroups.flatMap(
      group => group.interactions.map(
        interaction => FlattenedCustomer.apply(t.Id, t.Name, t.Surname, acc.AccountId, acc.AccountType,
          group.Id, group.date, interaction.Id, interaction.`type`.toString)
      )
    )
    )
}
