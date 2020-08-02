package com.github.seanhirisave.kafka.demo1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

import static java.util.Collections.singletonList;

public class ConsumerDemoAssignSeek {
  public static void main(String[] args) {
      final Logger logger = LoggerFactory.getLogger ( ConsumerDemoAssignSeek.class );

      String bootstrapServers = "127.0.0.1:9092";
      String topic = "first_topic";
      //   create consumer properties
      Properties properties = new Properties (  );
      properties.setProperty ( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
      properties.setProperty ( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName () );
      properties.setProperty ( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName ());
      properties.setProperty ( ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest" );
      //"earliest: read from very beginning","latest: read from the new messages onwards","none: throws error"

      //create consumer
      KafkaConsumer<String,String> consumer = new KafkaConsumer<> ( properties );

      //assign and seek are mostly used to replay data or fetch a specific message

      // assign
      TopicPartition partitionToReadFrom = new TopicPartition ( topic, 0 );
      consumer.assign ( singletonList ( partitionToReadFrom ) );

      //seek
      long offsetsToReadFrom = 15L;
      consumer.seek ( partitionToReadFrom, offsetsToReadFrom );

      int numberOfMessagesToRead = 5;
      boolean keepOnReading = true;
      int numberOfMessagesReadSoFar = 0;

      while (keepOnReading){
          ConsumerRecords<String, String> records =
                  consumer.poll ( Duration.ofMillis ( 100 ) );
          for ( ConsumerRecord<String, String> r : records ) {
              logger.info ( "Key: " + r.key ( ) + " Value: " + r.value ( ) );
              logger.info ( "Partition: " + r.partition ( ) + " Offset: " + r.offset ( ) );
              if (numberOfMessagesReadSoFar >= numberOfMessagesToRead) {
                  keepOnReading = false; // to exist the while loop
                  break;
              }
          }
      }
      logger.info ( "Exiting the Application" );
  }
}
