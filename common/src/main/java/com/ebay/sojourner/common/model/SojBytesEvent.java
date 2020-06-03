package com.ebay.sojourner.common.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SojBytesEvent implements Serializable {
  private byte[] messagekey;
  private byte[] message;
}
