package com.github.aleksandrachasch.avro.ops

import com.github.aleksandrachasch.avro.{Customer, ops}
import org.apache.spark.sql.Dataset

trait DataProcessor[T, C] {
  def customerFlatten(t: T): Seq[C]
}

object DataProcessor {

  def apply[T, C](implicit processor: DataProcessor[T, C]): DataProcessor[T, C] =
    processor

  implicit class DataProcessorOps[T, C](val t : T) extends AnyVal {
    def customerFlatten(implicit processor: DataProcessor[T, C]): Seq[C] = {
      processor.customerFlatten(t)
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
