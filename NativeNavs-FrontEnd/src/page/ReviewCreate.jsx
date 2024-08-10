import "./ReviewCreate.css";
import Tour_Item_mini_Review from "@/components/Tour_Item/Tour_Item_mini_Review";
import { useEffect, useReducer, useState } from "react";
import StarScoring from "@/components/Star/StarScoring";

const MAX_IMAGE_COUNT = 5; // 최대 이미지 업로드 수
const dummy_info = {
  tour: {
    // 투어 정보
    image:
      "https://cdn.pixabay.com/photo/2016/11/29/05/45/astronomy-1867616_960_720.jpg",
    title: "투어 제목",
    nav: {
      // 가이드 정보
      image:
        "https://cdn.pixabay.com/photo/2016/11/29/05/45/astronomy-1867616_960_720.jpg",
      nickname: "가이드이름",
    },
  },
  progress: {
    // 예약 정보
    date: "2021-09-01",
    participant: 2,
  },
};

const reducer = (state, action) => {
  switch (action.type) {
    case "score":
      return { ...state, score: action.score };
    case "description":
      return { ...state, description: action.description };
    case "image":
      return { ...state, image: action.image };
  }
};

const ReviewCreate = ({ info } = dummy_info) => {
  const [user, setUser] = useState(null);
  // 컴포넌트가 마운트될 때 localStorage에서 유저 정보를 가져옴
  useEffect(() => {
    setUser(JSON.parse(localStorage.getItem("user")));
  }, []);

  const [reviewData, dispatch] = useReducer(reducer, {
    score: 0,
    description: "",
    image: [], //이미지 파일 - Max 5개
  });
  const [tour_info, setTour_info] = useState({});

  const onImgChange = (e) => {
    const { files } = e.target;
    if (files && files.length > 0) {
      if (files.length > MAX_IMAGE_COUNT) {
        alert("최대 5개까지 업로드 가능합니다.");
        return;
      }
      const fileArr = [...files];
      for (let file of fileArr) {
        if (file.size > 1024 * 1024 * 10) {
          alert("10MB 이하의 파일만 업로드 가능합니다.");
          return;
        }
      }
      for (let imgData of reviewData.image) {
        URL.revokeObjectURL(imgData);
      }
      dispatch({ type: "image", image: fileArr });
    }
  };

  return (
    <div className="ReviewCreate">
      <section>
        <Tour_Item_mini_Review {...dummy_info} />
      </section>

      <section className="ScoreRating">
        <h4>
          {user.isKorean
            ? "Nav와 함께한 여행은 어땠나요?"
            : "How was your trip with Nav?"}
        </h4>
        <StarScoring
          onRatingChange={(score) => dispatch({ type: "score", score })}
        />
      </section>
      <section className="ReviewImgUploadSection">
        <div className="ReviewImgUploadHeader">
          <div>
            {user.isKorean == false ? "사진 등록 :" : "Upload Photos :"}
            <span>
              {reviewData.image.length} / {MAX_IMAGE_COUNT}
            </span>
          </div>
          <div>
            <label htmlFor="imgUploader">
              <div>+</div>
            </label>
            <input
              className="ReviewImgUploader"
              type="file"
              accept="image/*"
              multiple
              onChange={onImgChange}
              id="imgUploader"
              name="imgUploader"
            />
          </div>
        </div>
        <div className="ReviewImgUpload">
          {reviewData.image.length ? (
            reviewData.image.map((img, idx) => (
              <img key={idx} src={URL.createObjectURL(img)} alt="reviewImg" />
            ))
          ) : (
            <div className="noImgUploaded ">
              {user.isKorean
                ? "아직 등록한 이미지가 없습니다."
                : "No images uploaded yet."}
            </div>
          )}
        </div>
      </section>
      <section className="Reviewdescription">
        <div>
          {user.isKorean
            ? "솔직한 후기를 남겨 주세요"
            : "Please leave an honest review"}

          <span>
            {reviewData.description.length}/{200}자
          </span>
        </div>

        <textarea
          placeholder="리뷰를 작성해주세요."
          value={reviewData.description}
          onChange={(e) => {
            if (e.target.value.length <= 200) {
              dispatch({ type: "description", description: e.target.value });
            }
          }}
        />
      </section>
      <section className={`ReviewButtonSection`}>
        <button
          className={`ReviewCreateSend ${
            reviewData.image.length === 0 || reviewData.description === ""
              ? "notAbleButton"
              : ""
          }`}
          disabled={
            !(reviewData.image.length === 0 || reviewData.description !== "")
          }
          onClick={() => {
            console.log("HI");
          }}
        >
          {user.isKorean == false ? "제출" : "Submit"}
        </button>
      </section>
    </div>
  );
};

export default ReviewCreate;
