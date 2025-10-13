package com.greennext.solarestimater.model.mapper;

import com.greennext.solarestimater.model.Address;
import com.greennext.solarestimater.model.SolarPlant;
import com.greennext.solarestimater.model.dto.AddressDTO;
import com.greennext.solarestimater.model.dto.DailyEnergyDTO;
import com.greennext.solarestimater.model.dto.GraphDataPointDTO;
import com.greennext.solarestimater.model.dto.SolarPlantDTO;
import com.greennext.solarestimater.model.response.AllPlantsInfoResponseBody;
import com.greennext.solarestimater.model.response.PlantEnergyGenerationResponseBody;
import com.greennext.solarestimater.model.response.PlantEnergyGraphResponseBody;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ResponseBodyMapper {

    public List<SolarPlantDTO> mapToPlantDTOList(AllPlantsInfoResponseBody response) {
        if (response == null || response.getData() == null || response.getData().getPlant() == null) {
            return Collections.emptyList();
        }
        return response.getData().getPlant().stream()
                .map(this::toPlantDto)
                .collect(Collectors.toList());
    }

    private SolarPlantDTO toPlantDto(SolarPlant plant) {
        if (plant == null) return null;
        SolarPlantDTO dto = new SolarPlantDTO();
        dto.setPid(plant.getPid());
        dto.setUid(plant.getUid());
        dto.setName(plant.getName());
        dto.setStatus(plant.getStatus());
        dto.setAddress(toAddressDto(plant.getAddress()));
        dto.setNominalPower(plant.getNominalPower());
        dto.setEnergyYearEstimate(plant.getEnergyYearEstimate());
        dto.setDesignCompany(plant.getDesignCompany());
        dto.setPicBig(plant.getPicBig());
        dto.setPicSmall(plant.getPicSmall());
        dto.setInstall(plant.getInstall());
        dto.setGts(plant.getGts());
        return dto;
    }

    private AddressDTO toAddressDto(Address address) {
        if (address == null) return null;
        AddressDTO dto = new AddressDTO();
        dto.setCity(address.getCity());
        dto.setAddress(address.getAddress());
        dto.setTown(address.getTown());
        dto.setProvince(address.getProvince());
        dto.setVillage(address.getVillage());
        dto.setCountry(address.getCountry());
        dto.setLat(address.getLat());
        dto.setLon(address.getLon());
        dto.setTimezone(address.getTimezone());
        return dto;
    }

    public DailyEnergyDTO mapDailyEnergyDto(PlantEnergyGenerationResponseBody response) {
        if (response == null || response.getEnergyData() == null) {
            return null;
        }
        DailyEnergyDTO dto = new DailyEnergyDTO();
        dto.setSuccess(true);
        dto.setEnergy(response.getEnergyData().getEnergy());
        return dto;
    }

    public static List<GraphDataPointDTO> mapToGraphDataDTO(PlantEnergyGraphResponseBody responseBody) {
        if (responseBody == null) {
            return new ArrayList<>();
        }

        // Use the new helper method to get the correct list
        List<Map<String, String>> dataPoints = responseBody.extractDataPoints();

        if (dataPoints == null || dataPoints.isEmpty()) {
            return new ArrayList<>();
        }

        return dataPoints.stream()
                .map(point -> {
                    GraphDataPointDTO dto = new GraphDataPointDTO();
                    // The keys "val" and "ts" are consistent across all responses
                    dto.setValue(Double.parseDouble(point.get("val")));
                    dto.setTimestamp(point.get("ts"));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
