import React, { useState, useEffect, useRef } from "react";
import "./MapModal.css";
import { createRoot } from "react-dom/client";
import {
  APIProvider,
  ControlPosition,
  MapControl,
  AdvancedMarker,
  Map,
  Pin,
  useMapsLibrary,
  useAdvancedMarkerRef,
} from "@vis.gl/react-google-maps";

import { PlacePicker } from "@googlemaps/extended-component-library/react";

import { getStaticImage } from "@/utils/get-static-image";
const DEFAULT_CENTER = {
  // 서울역 좌표
  lat: 37.555167,
  lng: 126.970833,
};
const DEFAULT_ZOOM = 12; // 검색 전 지도 확대 수준
const DEFAULT_ZOOM_WITH_LOCATION = 16; // 검색 이루어 질 때 지도 확대 수준

const MapModal = () => {
  const pickerRef = useRef(null); // 자동 검색 결과 목록 중 선택된 것에 대한 값
  const [searchLocation, setSearchLocation] = useState(undefined); // 검색 결과 google PLACE 객체 저장
  const [selectedLocation, setSelectedLocation] = useState(undefined); // 위 검색 결과 PLACE의 좌표 및 리턴

  return (
    <div className="ModalBackground" onClick={(e) => e.preventDefault()}>
      <div className="mapModal">
        <section className="CloseModal">
          <img src={getStaticImage("close")} alt="" />
        </section>
        <section className="MapSection">
          {/* 지도 영역 */}
          <APIProvider
            apiKey={import.meta.env.VITE_GOOGLE_MAP_API_KEY}
            solutionChannel="GMP_devsite_samples_v3_rgmautocomplete"
            version="beta"
          >
            {/* 실제 지도 결과를 나타내줌 */}
            <Map
              id="gmap"
              mapId={import.meta.env.VITE_MAP_ID}
              center={searchLocation?.location ?? DEFAULT_CENTER}
              zoom={searchLocation ? DEFAULT_ZOOM_WITH_LOCATION : DEFAULT_ZOOM}
              zoomControl={false}
              gestureHandling="none"
              fullscreenControl={true}
              style={{ width: "80vw", height: "30vh", borderRadius: "10px" }}
            >
              {/* 검색 결과가 있을 경우 지도에 Marker표시 */}
              {searchLocation?.location && (
                <AdvancedMarker position={searchLocation?.location}>
                  <Pin
                    background={"#FBBC04"}
                    glyphColor={"#000"}
                    borderColor={"#000"}
                  />
                </AdvancedMarker>
              )}
            </Map>

            {/* 검색창 - 자동검색  */}
            <PlacePicker
              ref={pickerRef}
              forMap="gmap"
              country={["kr"]}
              language={"kr"}
              placeholder="장소를 입력해 주세요"
              style={{
                width: "80vw",
                height: "5vh",
                borderRadius: "10px",
                marginTop: "10px",
              }}
              onPlaceChange={(e) => {
                console.log(e.target.value);
                if (!pickerRef.current?.value) {
                  console.log("No place selected");
                  setSearchLocation(undefined);
                } else {
                  console.log(pickerRef.current?.value.formattedAddress);
                  console.log(pickerRef.current?.value.displayName);
                  console.log(pickerRef.current?.value.location.lat());
                  console.log(pickerRef.current?.value);
                  setSearchLocation(pickerRef.current?.value);
                  setSelectedLocation({
                    lat: pickerRef.current?.value.location.lat(),
                    lng: pickerRef.current?.value.location.lng(),
                    address:
                      pickerRef.current?.value.displayName ||
                      pickerRef.current?.value.formattedAddress,
                  });
                }
              }}
            />
          </APIProvider>
        </section>
        <section className="SearchResult">
          {selectedLocation ? (
            <div className="Searched">
              <h4>{selectedLocation.address}</h4>
              <div className="mapsearchButton">
                <button>등록하기</button>
              </div>
            </div>
          ) : (
            <div className="notSearch">
              검색해 주세요
              <p>장소 검색 결과가 여기에 뜹니다</p>
            </div>
          )}
        </section>
      </div>
    </div>
  );
};

export default MapModal;
