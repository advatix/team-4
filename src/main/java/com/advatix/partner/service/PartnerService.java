package com.advatix.partner.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advatix.partner.common.SelectionOption;
import com.advatix.partner.dto.PartnerSelectionDetails;
import com.advatix.partner.dto.PartnerSelectionResponse;
import com.advatix.partner.dto.SelectionRequest;
import com.advatix.partner.entity.CarrierServices;
import com.advatix.partner.entity.ShipmentRate;
import com.advatix.partner.entity.ZoneZipcodeMapping;
import com.advatix.partner.repo.AccountConfigRepo;
import com.advatix.partner.repo.CarrierServicesRepo;
import com.advatix.partner.repo.PartnersRepo;
import com.advatix.partner.repo.SelectionTypeMasterRepo;
import com.advatix.partner.repo.ShipmentRateRepo;
import com.advatix.partner.repo.ZoneMasterRepo;
import com.advatix.partner.repo.ZoneZipcodeMappingRepo;

@Service
public class PartnerService {

	@Autowired
	private AccountConfigRepo accountConfigRepo;

	@Autowired
	private CarrierServicesRepo carrierServicesRepo;

	@Autowired
	private PartnersRepo partnersRepo;

	@Autowired
	private SelectionTypeMasterRepo selectionTypeMasterRepo;

	@Autowired
	private ShipmentRateRepo shipmentRateRepo;

	@Autowired
	private ZoneMasterRepo zoneMasterRepo;

	@Autowired
	private ZoneZipcodeMappingRepo zoneZipcodeMappingRepo;

	public List<CarrierServices> getCarrierServices() {
		return carrierServicesRepo.findAll();
	}

	public List<ShipmentRate> getShipmentRatesByServiceId(long serviceId) {
		return shipmentRateRepo.findAllByServiceId(serviceId);
	}

	public PartnerSelectionResponse getSelectedPartnerDetails(SelectionRequest selectionRequest) {

		PartnerSelectionResponse partnerSelectionResponse = new PartnerSelectionResponse();

		Set<PartnerSelectionDetails> partnerSelectionDetailList = new LinkedHashSet<>();

		ZoneZipcodeMapping zoneZipcodeMapping = zoneZipcodeMappingRepo
				.findByZipCodeBeginLessThanEqualAndZipCodeEndGreaterThanEqual(selectionRequest.getShipToZipCode(),
						selectionRequest.getShipToZipCode());

		if (Objects.nonNull(zoneZipcodeMapping)) {

			String zoneCode = zoneZipcodeMapping.getZoneCode();

			List<CarrierServices> carrierServicesList = carrierServicesRepo
					.findAllByDeliveryDaysLessThanEqual(selectionRequest.getDeliveryDays());

			List<Long> serviceIds = new ArrayList<>();

			if (Objects.nonNull(carrierServicesList) && !carrierServicesList.isEmpty()) {

				for (CarrierServices carrierService : carrierServicesList) {

					serviceIds.add((long) carrierService.getId());
				}

				List<ShipmentRate> shipmentRates = shipmentRateRepo
						.findAllByZoneCodeAndServiceIdInAndPkgWeightMinLessThanEqualAndPkgWeightMaxGreaterThanEqualOrderByPrice(
								zoneCode, serviceIds, selectionRequest.getPkgWeight(), selectionRequest.getPkgWeight());

				long deliveryDays = 0;
				int index = 0;
				double price = 0.0;
				
				if (Objects.nonNull(shipmentRates) && !shipmentRates.isEmpty()) {

					

					for (int i = 0; i < shipmentRates.size(); i++) {

						PartnerSelectionDetails partnerSelectionDetail = new PartnerSelectionDetails();
						CarrierServices carrierService = carrierServicesRepo
								.getById((int) shipmentRates.get(i).getServiceId());
						partnerSelectionDetail.setCarrierName(carrierService.getCarrierName());
						partnerSelectionDetail.setDeliveryDays((int) carrierService.getDeliveryDays());
						partnerSelectionDetail.setRate(shipmentRates.get(i).getPrice());
						partnerSelectionDetail.setShipMethod(carrierService.getServiceName());

						if (SelectionOption.Cheapest.name().equalsIgnoreCase(selectionRequest.getSelectionOption())
								&& i == 0) {

							index = i;
						}
						if (SelectionOption.Fastest.name().equalsIgnoreCase(selectionRequest.getSelectionOption())) {

							if(i==0) {
							deliveryDays = partnerSelectionDetail.getDeliveryDays();
							index = i;
							}else if(i > 0 && partnerSelectionDetail.getDeliveryDays() < deliveryDays) {
								
								deliveryDays = partnerSelectionDetail.getDeliveryDays();
								index = i;
							}
						}
						if (SelectionOption.CheapestAndFastest.name()
								.equalsIgnoreCase(selectionRequest.getSelectionOption())) {

							if (i > 0 && price == shipmentRates.get(i).getPrice()
									&& partnerSelectionDetail.getDeliveryDays() < deliveryDays) {

								deliveryDays = partnerSelectionDetail.getDeliveryDays();
								index = i;
								price = shipmentRates.get(i).getPrice();

							} else if (i == 0) {

								deliveryDays = partnerSelectionDetail.getDeliveryDays();
								index = i;
								price = shipmentRates.get(i).getPrice();
							}

						}

						partnerSelectionDetailList.add(partnerSelectionDetail);

					}

					partnerSelectionResponse.setPreferedIndex(index);

					partnerSelectionResponse.setPartnerSelectionList(partnerSelectionDetailList);

				}

			}

		}
		return partnerSelectionResponse;
	}

	

}
