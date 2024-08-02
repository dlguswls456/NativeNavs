import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Rating from "../Star/Rating(Basic)";
import Heart from "../Heart/Heart";
import styles from "./Tour_Item.module.css";
import { getStaticImage } from "@/utils/get-static-image";

const Tour_Item = ({
  tourId,
  userId,
  title,
  thumbnailImage,
  startDate,
  endDate,
  reviewAverage,
  nav_profile_img,
  nav_nickname,
  navigateFragment,
  user, // 추가: user 정보를 props로 받음
}) => {
  const navigate = useNavigate();
  const [isWishListed, setIsWishListed] = useState(false);

  // 투어 클릭 이벤트
  const onClickTour = (e) => {
    e.stopPropagation(); // 이벤트 전파 방지
    if (user) {
      // 네이티브 안드로이드 브릿지를 사용해 투어 상세 페이지로 이동
      navigateFragment(parseInt(tourId), parseInt(user.userId));
    } else {
      console.error("User 정보가 없습니다.");
    }
  };

  // 위시리스트 이벤트
  const toggleWishlist = (e) => {
    e.stopPropagation();
    setIsWishListed((current) => !current);
  };

  return (
    <div onClick={onClickTour} className={styles.tour_item}>
      {/* 투어 이미지 */}
      <div className={styles.thumbnail_container}>
        <img src={thumbnailImage} alt="" className={styles.tour_thumbnail} />
        <div className={styles.heart_container}>
          <Heart
            isWishListed={isWishListed}
            setIsWishListed={setIsWishListed}
            onClickEvent={toggleWishlist}
          />
        </div>
      </div>

      {/* 투어 정보 */}
      <section className={styles.tour_info}>
        {/* 왼쪽 정보 */}
        <div className={styles.tour_leftinfo}>
          <p className={styles.tour_title}>{title}</p>
          <p className={styles.tour_duration}>
            {startDate} ~ {endDate}
          </p>
          <Rating avg={reviewAverage} />
        </div>
        {/* 오른쪽 정보 */}
        <div className={styles.tour_rightinfo}>
          <div className={styles.tour_nav}>
            {/* Nav 프로필 이미지 */}
            <img
              src={nav_profile_img}
              alt={nav_nickname}
              className={styles.nav_img}
            />
            {/* Nav 닉네임 */}
            <p style={{ cursor: "pointer" }}>{nav_nickname}</p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Tour_Item;
