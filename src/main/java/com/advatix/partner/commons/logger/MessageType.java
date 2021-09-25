package com.advatix.partner.commons.logger;

import com.advatix.partner.commons.utils.Constant;

public enum MessageType {

  /** The Api request. */
  ApiRequest,
  /** The Api request response. */
  ApiRequestResponse,
  /** The Info. */
  Info,
  /** The Debug. */
  Debug,
  /** The Error. */
  Error;

  public String getLogName(Logger logger) {
    return String.format("%s - %s - %s", logger.getValue(Constant.REQUEST_KEY), this.name(),
        logger.getValue(Constant.REQUEST_ID));
  }

}
