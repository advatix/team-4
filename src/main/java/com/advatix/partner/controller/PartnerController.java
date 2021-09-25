package com.advatix.partner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.advatix.partner.commons.exceptions.BaseException;
import com.advatix.partner.commons.utils.RestResponse;
import com.advatix.partner.commons.utils.RestUtils;
import com.advatix.partner.dto.PartnerSelectionResponse;
import com.advatix.partner.dto.SelectionRequest;
import com.advatix.partner.entity.CarrierServices;
import com.advatix.partner.entity.ShipmentRate;
import com.advatix.partner.service.PartnerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = "/partner")
@Api(value = "/partner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, tags = {
		"Partner Api's" }, hidden = false)
@ApiResponses(value = { @ApiResponse(code = 401, message = "Not Authorized"),
		@ApiResponse(code = 403, message = "Not Authenticated"), @ApiResponse(code = 404, message = "Not found"),
		@ApiResponse(code = 500, message = "Internal Server Error") })
public class PartnerController {

	@Autowired
	private PartnerService partnerService;

	@ApiOperation(value = "Get Carrier Services", response = String.class, httpMethod = "GET", notes = "Get Carrier Services")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get Carrier Services ", response = String.class) })
	@GetMapping(path = "/getCarrierServices")
	@ResponseBody
	public ResponseEntity<RestResponse<List<CarrierServices>>> getCarrierServices() throws BaseException {
		return RestUtils.successResponse(partnerService.getCarrierServices());
	}

	@ApiOperation(value = "Get Shipment Rates by serviceId", response = String.class, httpMethod = "GET", notes = "Get Shipment Rates by serviceId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get Shipment Rates by serviceId", response = String.class) })
	@GetMapping(path = "/getShipmentRatesByServiceId")
	@ResponseBody
	public ResponseEntity<RestResponse<List<ShipmentRate>>> getShipmentRatesByServiceId(
			@RequestParam(value = "serviceId", required = false) long serviceId) throws BaseException {
		return RestUtils.successResponse(partnerService.getShipmentRatesByServiceId(serviceId));
	}
	
	
	@ApiOperation(value = "get Selected Partner Details", response = String.class, httpMethod = "POST", notes = "get Selected Partner Details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get Selected Partner Details", response = String.class)})
    @PostMapping(path = "/getSelectedPartnerDetails")
    @ResponseBody
    public ResponseEntity<RestResponse<PartnerSelectionResponse>> updateOrderStatus(@RequestBody SelectionRequest selectionRequest)
            throws BaseException {
        return RestUtils.successResponse(partnerService.getSelectedPartnerDetails(selectionRequest));
    }

}
