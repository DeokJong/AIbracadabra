package com.ssafy.ai.tools;

import com.ssafy.model.dto.domain.Document;
import com.ssafy.model.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TourInfoApiToolsImpl implements TourInfoApiTools {
  private final MapService mapService;

  @Override
  public List<Coordinates> searchLocationByKeyword(String keyword) {
    return mapService.searchByKeyword(keyword, 1)
            .getDocuments()
            .stream()
            .map(e -> new Coordinates(e.getX(), e.getY(), e.getAddress_name()))
            .toList();
  }

  @Override
  public List<Document.Item> searchContentByLocation(String mapX, String mapY, String contentTypeID, String meter) {
    return mapService.searchContent(mapX,mapY,contentTypeID,1,meter).block().getDocuments();
  }

}
