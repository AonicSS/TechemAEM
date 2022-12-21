export const INCREASE_APP_STEP = 'INCREASE_APP_STEP';
export const DECREASE_APP_STEP = 'DECREASE_APP_STEP';
export const SET_APP_STEP = 'SET_APP_STEP';
export const SET_BUTTON_ACTIVE = 'SET_BUTTON_ACTIVE';
export const INCREASE_RENTINGS_STEP = 'INCREASE_RENTINGS_STEP';
export const INCREASE_RENTINGS = 'INCREASE_RENTINGS';
export const DECREASE_RENTINGS = 'DECREASE_RENTINGS';
export const UPDATE_POSTAL_CODE = 'UPDATE_POSTAL_CODE';
export const SET_ANSWER = 'SET_ANSWER';
export const SET_MODAL = 'SET_MODAL';
export const SET_YEARS = 'SET_YEARS';
export const INCREASE_ROOMS = 'INCREASE_BEDROOMS';
export const DECREASE_ROOMS = 'DECREASE_BEDROOMS';
export const SET_ROOMS = 'SET_ROOMS';
export const ADD_HOUSE = 'ADD_HOUSE';
export const SET_PRICING = 'SET_PRICING';
export const SET_REGION = 'SET_REGION';

import { PostalCode } from '../reducers/App';

export function increaseAppStep() {
	return {
		type: INCREASE_APP_STEP,
	};
}

export function decreaseAppStep() {
	return {
		type: DECREASE_APP_STEP,
	};
}

export function setAppStep(step: number) {
	return {
		type: SET_APP_STEP,
		payload: {
			step,
		},
	};
}

export function setButtonActive(nextButtonActive: any, questionName: string) {
	return {
		type: SET_BUTTON_ACTIVE,
		payload: {
			questionName,
			nextButtonActive,
		},
	};
}

export function increaseRentings(btnActive: boolean, questionName: string) {
	return {
		type: INCREASE_RENTINGS,
		payload: {
			questionName,
			btnActive,
		},
	};
}

export function increaseRentingStep() {
	return {
		type: INCREASE_RENTINGS_STEP,
	};
}

export function decreaseRentings(btnActive: boolean, questionName: string) {
	return {
		type: DECREASE_RENTINGS,
		payload: {
			questionName,
			btnActive,
		},
	};
}

export function updatePostalCode(postalCode: PostalCode) {
	return {
		type: UPDATE_POSTAL_CODE,
		payload: {
			postalCode,
		},
	};
}

export function showModal(showModal: boolean) {
	return {
		type: SET_MODAL,
		payload: {
			showModal,
		},
	};
}

export function setYears(years: number) {
	return {
		type: SET_YEARS,
		payload: {
			years,
		},
	};
}

export function setAnswers(
	btnActive: boolean,
	choice: any,
	questionName: string
) {
	return {
		type: SET_ANSWER,
		payload: {
			questionName,
			choice,
			btnActive,
		},
	};
}

export function increaseRooms(
	roomName: string,
	questionName: string,
	index: number
) {
	return {
		type: INCREASE_ROOMS,
		payload: {
			questionName,
			roomName,
			index,
		},
	};
}

export function decreaseRooms(
	roomName: string,
	questionName: string,
	index: number
) {
	return {
		type: DECREASE_ROOMS,
		payload: {
			questionName,
			roomName,
			index,
		},
	};
}

export function addHouse(questionName: string) {
	return {
		type: ADD_HOUSE,
		payload: { questionName },
	};
}

export function setRooms(
	amount: string,
	roomName: string,
	questionName: string
) {
	return {
		type: SET_ROOMS,
		payload: {
			questionName,
			roomName,
			amount,
		},
	};
}

export function setPricing(pricing: string) {
	return {
		type: SET_PRICING,
		payload: {
			pricing,
		},
	};
}

export function setRegion(questionName: string, value: boolean) {
	return {
		type: SET_REGION,
		payload: { questionName, value },
	};
}
