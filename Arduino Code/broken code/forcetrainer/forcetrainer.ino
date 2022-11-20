#include <stdio.h>
#define SYNC 0xAA
#define EXCODE 0x55

void setup() {
  Serial.begin(9600);
}


int parsePayload( unsigned char *payload, unsigned char pLength ) {
  unsigned char bytesParsed = 0;
  unsigned char code;
  unsigned char codeLength;
  unsigned char extendedCodeLevel;
  int i;
  
  /* Loop until all bytes are parsed from the payload[] array... */
  while( bytesParsed < pLength ) {
    
    /* Parse the extendedCodeLevel, code, and length */
    extendedCodeLevel = 0;
    while( payload[bytesParsed] == EXCODE ) {
      extendedCodeLevel++;
      bytesParsed++;
    }
    
  code = payload[bytesParsed++];
  if( code & 0x80 ) codeLength = payload[bytesParsed++];
  else codeLength = 1;
  
  /* TODO: Based on the extendedCodeLevel, code, length,
  * and the [CODE] Definitions Table, handle the next
  * "length" bytes of data from the payload as
  * appropriate for your application.
  */
  //Serial.print( "EXCODE level: %d CODE: 0x%02X length: %d\n", extendedCodeLevel, code, codeLength );
  Serial.print( "EXCODE level:");
  Serial.print(extendedCodeLevel);
  Serial.print("CODE: ");
  Serial.print(code);
  Serial.print("length: ");
  Serial.print(codeLength);
  Serial.print( "Data value(s):" );
  
  for( i=0; i<codeLength; i++ ) {
    Serial.print( " %02X");
    Serial.print(payload[bytesParsed+i] & 0xFF );
  }
  Serial.println( "\n" );
  
  /* Increment the bytesParsed by the length of the Data Value */
  bytesParsed += codeLength;
  }

  return( 0 );
}

void loop() {
  int checksum;
  unsigned char payload[256];
  unsigned char pLength;
  unsigned char c;
  unsigned char i;
  
  /* TODO: Initialize 'stream' here to read from a serial data
  * stream, or whatever stream source is appropriate for your
  * application. See documentation for "Serial I/O" for your
  * platform for details.
  */
  Serial.println("HI");
  if (Serial.available() > 0) {
 
  /* Loop forever, parsing one Packet per loop... */
  while( 1 ) {
    /* Synchronize on [SYNC] bytes */
    //fread( &c, 1, 1, stream );
    Serial.readBytes(&c, 1);
    
    if( c != SYNC ) continue;
    //fread( &c, 1, 1, stream );
    Serial.readBytes(&c, 1);
    if( c != SYNC ) continue;
    
    /* Parse [PLENGTH] byte */
    while( true ) {
      //fread( &pLength, 1, 1, stream );
      Serial.readBytes(&pLength, 1);
      if( pLength != 170 ) {
        Serial.println("BREAK 170");
        break;
      }
    }
    if( pLength > 169 ) continue;
    
    /* Collect [PAYLOAD...] bytes */
    //fread( payload, 1, pLength, stream );
    Serial.readBytes(payload, pLength);
    
    /* Calculate [PAYLOAD...] checksum */
    checksum = 0;
    for( i=0; i<pLength; i++ ) checksum += payload[i];
    checksum &= 0xFF;
    checksum = ~checksum & 0xFF;
    
    /* Parse [CKSUM] byte */
    //fread( &c, 1, 1, stream );
    Serial.readBytes(&c, 1);
    
    /* Verify [CKSUM] byte against calculated [PAYLOAD...] checksum */
    if( c != checksum ) {
      Serial.println("CHECKSUM OK");
      continue;
    }
    
    /* Since [CKSUM] is OK, parse the Data Payload */
    //parsePayload( payload, pLength );
    //}
    for(int i = 0; i < pLength; i++) {
      Serial.print(payload[i]);
  }
    Serial.print("\n");
  }
}
}
