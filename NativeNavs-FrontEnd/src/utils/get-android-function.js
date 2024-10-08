export function navigateToReservationListFragmentReservationDetail(
  tour_id,
  reservation_id
) {
  if (
    window.Android &&
    typeof window.Android.navigateToReservationListFragmentReservationDetail ===
      "function"
  ) {
    window.Android.navigateToReservationListFragmentReservationDetail(
      tour_id,
      reservation_id
    );
  } else {
    console.log(
      "navigateToReservationListFragmentReservationDetail function is not defined"
    );
  }
}

export function navigateToReservationListFragmentTourList() {
  if (
    window.Android &&
    typeof window.Android.navigateToReservationListFragmentTourList ===
      "function"
  ) {
    window.Android.navigateToReservationListFragmentTourList();
  } else {
    console.log(
      "navigateToReservationListFragmentTourList function is not defined"
    );
  }
}

export function navigateToReservationDetailChattingRoom(chat_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToReservationDetailChattingRoom === "function"
  ) {
    window.Android.navigateToReservationDetailChattingRoom(chat_id);
  } else {
    console.log(
      "navigateToReservationDetailChattingRoom function is not defined"
    );
  }
}

export function navigateBack() {
  if (window.Android && typeof window.Android.navigateBack === "function") {
    window.Android.navigateBack();
  } else {
    console.log("navigateBack function is not defined");
  }
}

export function navigateFromWishToTourListFragment() {
  if (
    window.Android &&
    typeof window.Android.navigateFromWishToTourListFragment === "function"
  ) {
    window.Android.navigateFromWishToTourListFragment();
  } else {
    console.log("navigateFromWishToTourListFragment function is not defined");
  }
}

export function navigateToWishDetailFragment(tour_id, user_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToWishDetailFragment === "function"
  ) {
    window.Android.navigateToWishDetailFragment(tour_id, user_id);
  } else {
    console.log("navigateToWishDetailFragment function is not defined");
  }
}

export function navigateToTravReviewPhotoFragment(trav_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToTravReviewPhotoFragment === "function"
  ) {
    window.Android.navigateToTravReviewPhotoFragment(trav_id);
  } else {
    console.log("navigateToTravReviewPhotoFragment function is not defined");
  }
}

export function navigateToTourModifyFragment(tour_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToTourModifyFragment === "function"
  ) {
    window.Android.navigateToTourModifyFragment(tour_id);
  } else {
    console.log("navigateToTourModifyFragment function is not defined");
  }
}

export function navigateToTourListFragment() {
  if (
    window.Android &&
    typeof window.Android.navigateToTourListFragment === "function"
  ) {
    window.Android.navigateToTourListFragment();
  } else {
    console.log("navigateToTourListFragment function is not defined");
  }
}

export function moveFromTourRegisterToTourDetailFragment(tourId, navId) {
  if (
    window.Android &&
    typeof window.Android.moveFromTourRegisterToTourDetailFragment ===
      "function"
  ) {
    window.Android.moveFromTourRegisterToTourDetailFragment(tourId, navId);
  } else {
    console.log(
      "moveFromTourRegisterToTourDetailFragment function is not defined"
    );
  }
}

export function showRegisterFailDialog() {
  if (
    window.Android &&
    typeof window.Android.showRegisterFailDialog === "function"
  ) {
    window.Android.showRegisterFailDialog();
  } else {
    console.log("showRegisterFailDialog function is not defined");
  }
}

export function navigateToTourDetailFragment(tour_id, user_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToTourDetailFragment === "function"
  ) {
    window.Android.navigateToTourDetailFragment(tour_id, user_id);
  } else {
    console.log("navigateToTourDetailFragment function is not defined");
  }
}

export function navigateToTourReviewPhotoFragment(tour_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToTourReviewPhotoFragment === "function"
  ) {
    window.Android.navigateToTourReviewPhotoFragment(tour_id);
  } else {
    console.log("navigateToTourReviewPhotoFragment function is not defined");
  }
}

export function navigateToNavReviewPhotoFragment(nav_id) {
  if (
    window.Android &&
    typeof window.Android.navigateToNavReviewPhotoFragment === "function"
  ) {
    window.Android.navigateToNavReviewPhotoFragment(nav_id);
  } else {
    console.log("navigateToNavReviewPhotoFragment function is not defined");
  }
}

export function navigateToMyTripDetailFragment(tourId) {
  if (
    window.Android &&
    typeof window.Android.navigateToMyTripDetailFragment === "function"
  ) {
    window.Android.navigateToMyTripDetailFragment(tourId);
  } else {
    console.log("navigateToMyTripDetailFragment function is not defined");
  }
}

export function navigateToMyTripListToTourRegisterFragment(tourId) {
  if (
    window.Android &&
    typeof window.Android.navigateToMyTripListToTourRegisterFragment ===
      "function"
  ) {
    window.Android.navigateToMyTripListToTourRegisterFragment(tourId);
  } else {
    console.log(
      "navigateToMyTripListToTourRegisterFragment function is not defined"
    );
  }
}

export function navigateToReservationDetailFragment(tourId) {
  if (
    window.Android &&
    typeof window.Android.navigateToReservationDetailFragment === "function"
  ) {
    window.Android.navigateToReservationDetailFragment(tourId);
  } else {
    console.log("navigateToReservationDetailFragment function is not defined");
  }
}

export function navigateFromTourModifyToTourDetailFragment(tourId, navId) {
  if (
    window.Android &&
    typeof window.Android.navigateFromTourModifyToTourDetailFragment ===
      "function"
  ) {
    window.Android.navigateFromTourModifyToTourDetailFragment(tourId, navId);
  } else {
    console.log(
      "navigateFromTourModifyToTourDetailFragment function is not defined"
    );
  }
}

export function showModifyFailDialog() {
  if (
    window.Android &&
    typeof window.Android.showModifyFailDialog === "function"
  ) {
    window.Android.showModifyFailDialog();
  } else {
    console.log("showModifyFailDialog function is not defined");
  }
}

export function navigateToReservationRegisterChattingRoom() {
  if (
    window.Android &&
    typeof window.Android.navigateToReservationRegisterChattingRoom ===
      "function"
  ) {
    window.Android.navigateToReservationRegisterChattingRoom();
  } else {
    console.log(
      "navigateToReservationRegisterChattingRoom function is not defined"
    );
  }
}
export function moveFromReviewRegisterToReviewListFragment(tourId) {
  if (
    window.Android &&
    typeof window.Android.moveFromReviewRegisterToReviewListFragment ===
      "function"
  ) {
    window.Android.moveFromReviewRegisterToReviewListFragment(tourId);
  } else {
    console.log(
      "moveFromReviewRegisterToReviewListFragment function is not defined"
    );
  }
}

export function showReviewRegisterFailDialog() {
  if (
    window.Android &&
    typeof window.Android.showReviewRegisterFailDialog === "function"
  ) {
    window.Android.showReviewRegisterFailDialog();
  } else {
    console.log("showReviewRegisterFailDialog function is not defined");
  }
}

export function navigateToReservationRegisterDetailFragment(
  tourId,
  reservationId
) {
  if (
    window.Android &&
    typeof window.Android.navigateToReservationRegisterDetailFragment ===
      "function"
  ) {
    window.Android.navigateToReservationRegisterDetailFragment(
      tourId,
      reservationId
    );
  } else {
    console.log(
      "navigateToReservationRegisterDetailFragment function is not defined"
    );
  }
}

export function showReservationRegisterFailDialog() {
  if (
    window.Android &&
    typeof window.Android.showReservationRegisterFailDialog === "function"
  ) {
    window.Android.showReservationRegisterFailDialog();
  } else {
    console.log("showReservationRegisterFailDialog function is not defined");
  }
}
