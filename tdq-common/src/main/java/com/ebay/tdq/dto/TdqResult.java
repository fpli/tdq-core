package com.ebay.tdq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author juntzhang
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TdqResult {
  @Builder.Default
  private Code code = Code.OK;
  private Exception exception;
  private String msg;

  public boolean isOk() {
    return code == Code.OK && exception == null;
  }

  public TdqResult timeout() {
    this.code = Code.TIMEOUT;
    this.msg  = Code.TIMEOUT.name().toLowerCase();
    return this;
  }

  public TdqResult failed() {
    this.code = Code.FAILED;
    return this;
  }

  public TdqResult exception(Exception e) {
    this.code      = Code.EXCEPTION;
    this.exception = e;
    this.msg       = e.getMessage();
    return this;
  }

  @Getter
  @AllArgsConstructor
  public enum Code {
    OK(0), FAILED(1), TIMEOUT(2), EXCEPTION(3);
    private final int code;
  }
}
