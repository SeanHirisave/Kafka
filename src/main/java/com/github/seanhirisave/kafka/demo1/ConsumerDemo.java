package com.github.seanhirisave.kafka.demo1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

import static java.util.Arrays.asList;

public class ConsumerDemo {
  public static void main(String[] args) {
      final Logger logger = LoggerFactory.getLogger ( ConsumerDemo.class );

      String bootstrapServers = "127.0.0.1:9092";
      String groupId = "my-fifth-application";
      String topic = "first_topic";
      //   create consumer properties
      Properties properties = new Properties (  );
      properties.setProperty ( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
      properties.setProperty ( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName () );
      properties.setProperty ( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName ());
      properties.setProperty ( ConsumerConfig.GROUP_ID_CONFIG,groupId );
      properties.setProperty ( ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest" );
      //"earliest: read from very beginning","latest: read from the new messges onwards","none: throws error"

      //create consumer

      KafkaConsumer<String,String> consumer = new KafkaConsumer<> ( properties );

      //subscribe consumer to our topic(s)

//      consumer.subscribe ( singleton ( topic ) );
      consumer.subscribe ( asList(topic) );
      //poll for new data

      while (true){
          ConsumerRecords<String, String> records =
                  consumer.poll ( Duration.ofMillis ( 100 ) );
          records.forEach ( r ->{
              logger.info ( "Key: "+ r.key () +"Value: "+r.value () );
              logger.info ( "Partition: "+ r.partition () + "Offset: "+ r.offset ());
          } );
      }
  }
}
