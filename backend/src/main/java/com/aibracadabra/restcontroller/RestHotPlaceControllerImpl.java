package com.aibracadabra.restcontroller;

import com.aibracadabra.model.dto.domain.HotPlace;
import com.aibracadabra.model.service.HotPlaceService;
import com.aibracadabra.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/hotPlace")
@RequiredArgsConstructor
public class RestHotPlaceControllerImpl implements ResponseEntityHelper, RestHotPlaceController {

  private final HotPlaceService hotPlaceService;

  @Override
  public ResponseEntity<?> registerHotPlace(HotPlace hotPlace, MultipartFile image, CustomUserDetails user) {
    hotPlace.setMno(user.getMember().getMno());
    hotPlaceService.registerHotPlace(hotPlace, image);
    return handleResponse("CREATED", HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<?> updateHotPlace(HotPlace hotPlace, MultipartFile image,Integer hno, CustomUserDetails user) {
    hotPlace.setMno(user.getMember().getMno());
    hotPlace.setHno(hno);
    hotPlaceService.update(hotPlace, image);
    return handleResponse( "OK", HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<?> searchByLocation(String mapX, String mapY, String meter) {
    return handleResponse(hotPlaceService.findByLocation(mapX, mapY, meter), "OK", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<?> searchByHno(Integer hno) {
    return handleResponse(hotPlaceService.findByHno(hno), "OK", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<?> deleteByHno(Integer hno, CustomUserDetails user) {
    hotPlaceService.deleteByHno(hno, user.getMember().getMno());
    return handleResponse("NO_CONTENT", HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<?> likeHotPlace(Integer hno, CustomUserDetails user) {
    int mno = user.getMember().getMno();
    hotPlaceService.likeHotPlace(mno, hno);
    return handleResponse("LIKED", HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<?> unlikeHotPlace(Integer hno, CustomUserDetails user) {
    int mno = user.getMember().getMno();
    hotPlaceService.unlikeHotPlace(mno, hno);
    return handleResponse("UNLIKED", HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<?> findMyLikedHotPlaces(CustomUserDetails user) {
    int mno = user.getMember().getMno();
    return handleResponse(hotPlaceService.findLikedHotPlaceIdsByMember(mno), "OK", HttpStatus.OK);
  }

}
