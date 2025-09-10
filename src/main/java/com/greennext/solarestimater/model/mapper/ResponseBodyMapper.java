package com.greennext.solarestimater.model.mapper;

import com.greennext.solarestimater.model.Address;
import com.greennext.solarestimater.model.SolarPlant;
import com.greennext.solarestimater.model.dto.AddressDTO;
import com.greennext.solarestimater.model.dto.DailyEnergyDTO;
import com.greennext.solarestimater.model.dto.SolarPlantDTO;
import com.greennext.solarestimater.model.response.AllPlantsInfoResponseBody;
import com.greennext.solarestimater.model.response.PlantEnergyPerDayResponseBody;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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

    public DailyEnergyDTO mapDailyEnergyDto(PlantEnergyPerDayResponseBody response) {
        if (response == null || response.getDailyEnergyData() == null) {
            return null;
        }
        DailyEnergyDTO dto = new DailyEnergyDTO();
        dto.setSuccess(true);
        dto.setEnergy(response.getDailyEnergyData().getEnergy());
        return dto;
    }
}
