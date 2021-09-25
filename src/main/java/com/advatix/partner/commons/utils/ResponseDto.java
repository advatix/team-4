/*
 * @author Advatix
 * @since 2019
 * @version 1.0
 */
package com.advatix.partner.commons.utils;

import java.util.List;

/**
 * The Class ResponseDto.
 */
public class ResponseDto {

  /** The entity id. */
  private String entityId;

  /** The error message. */
  private List<String> errorMessage;

  /** The row number. */
  private int rowNumber;

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public List<String> getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(List<String> errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(int rowNumber) {
    this.rowNumber = rowNumber;
  }

  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "ResponseDto [entityId=" + entityId + ", errorMessage=" + errorMessage + ", rowNumber=" + rowNumber + "]";
  }

}
