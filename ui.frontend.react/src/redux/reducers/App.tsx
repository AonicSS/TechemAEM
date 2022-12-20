import { questions, rooms } from '../../components/Form/Questions';
import { trueTypeOf, validPostalCode } from '../../utils/helpers';
import {
	INCREASE_APP_STEP,
	DECREASE_APP_STEP,
	SET_APP_STEP,
	INCREASE_RENTINGS_STEP,
	INCREASE_RENTINGS,
	DECREASE_RENTINGS,
	UPDATE_POSTAL_CODE,
	SET_MODAL,
	SET_ANSWER,
	SET_YEARS,
	INCREASE_ROOMS,
	DECREASE_ROOMS,
	SET_ROOMS,
	ADD_HOUSE,
	SET_PRICING,
	SET_REGION,
} from '../actions/App';

import { getAlarmNumberForHouse } from '../../utils/helpers';

const initialState = {
	step: 1,
	maxSteps: 5,
	rentings: 1,
	maxRentings: 2,
	showModal: false,
	acceptContact: false,
	acceptMarketing: false,
	years: 10,
	postalCode: { code: '', valid: false, area: '' },
	questions: questions,
	pricing: '',
};

export interface PostalCode {
	code: string;
	valid: boolean;
	area: string;
}

export interface Question {
	question: string;
	choice: any;
	type: string;
	btnActive: boolean;
	answers: Answers[];
}

export interface Answers {
	name: string;
	type: string;
	amount: number;
	required: boolean;
	house: number;
}
export interface AppReduxStoreProps {
	appData: {
		step: number;
		rentings: number;
		maxSteps: number;
		maxRentings: number;
		years: number;
		showModal: boolean;
		postalCode: PostalCode;
		acceptContact: boolean;
		acceptMarketing: boolean;
		questions: Question[];
		pricing: string;
	};
}

const appData = (
	state = initialState,
	action: { type: any; payload?: any }
) => {
	switch (action.type) {
		case INCREASE_APP_STEP:
			return {
				...state,
				step:
					state.step < state.maxSteps
						? state.step + 1
						: state.maxSteps,
			};
		case DECREASE_APP_STEP:
			return {
				...state,
				step: state.step > 1 ? state.step - 1 : 1,
			};
		case SET_APP_STEP:
			return {
				...state,
				step: action.payload.step,
			};
		case INCREASE_RENTINGS:
			return {
				...state,
				maxRentings: state.maxRentings + 1,
				questions: state.questions.map((q, i) => {
					return q.question === action.payload.questionName
						? {
								...q,
								choice:
									trueTypeOf(state.questions[i].choice) ===
									'string'
										? //@ts-ignore
										  parseInt(state.questions[i].choice) +
										  1
										: //@ts-ignore
										  state.questions[i].choice + 1,
								btnActive: action.payload.btnActive,
						  }
						: { ...q };
				}),
			};
		case INCREASE_RENTINGS_STEP:
			return {
				...state,
				rentings:
					state.rentings < state.maxRentings
						? state.rentings + 1
						: state.maxRentings,
			};
		case DECREASE_RENTINGS:
			return {
				...state,
				maxRentings: state.maxRentings > 2 ? state.maxRentings - 1 : 2,
				questions: state.questions.map((q, i) => {
					return q.question === action.payload.questionName
						? {
								...q,
								choice:
									//@ts-ignore
									state.questions[i].choice > 0
										? //@ts-ignore
										  state.questions[i].choice - 1
										: 0,
								// btnActive: action.payload.btnActive,
						  }
						: { ...q };
				}),
			};
		case UPDATE_POSTAL_CODE:
			return {
				...state,
				postalCode: action.payload.postalCode,
			};
		case SET_MODAL:
			return {
				...state,
				showModal: action.payload.showModal,
			};
		case SET_YEARS:
			return {
				...state,
				years: action.payload.years,
			};
		case SET_PRICING:
			return {
				...state,
				pricing: action.payload.pricing,
			};
		case SET_ANSWER:
			const isPostalCodevalid = validPostalCode(
				action.payload.choice.toString()
			);
			if (
				action.payload.questionName ===
				'Wie viele Wohneinheiten möchten Sie ausstatten?'
			) {
				return {
					...state,
					rentings:
						action.payload.choice === ''
							? 0
							: parseInt(action.payload.choice),
					questions: state.questions.map((q, i) => {
						return q.question === action.payload.questionName
							? {
									...q,
									choice: action.payload.choice,
									btnActive:
										i !== 3
											? action.payload.btnActive
											: isPostalCodevalid,
							  }
							: { ...q };
					}),
				};
			} else {
				return {
					...state,
					questions: state.questions.map((q, i) => {
						return q.question === action.payload.questionName
							? {
									...q,
									choice: action.payload.choice,
									btnActive:
										i !== 1
											? action.payload.btnActive
											: isPostalCodevalid,
							  }
							: { ...q };
					}),
				};
			}
		case SET_ROOMS: {
			return {
				...state,
				questions: state.questions.map((q) => {
					return q.question === action.payload.questionName
						? {
								...q,
								answers: q.answers.map((a) => {
									return a.name === action.payload.roomName
										? {
												...a,
												amount: action.payload.amount,
										  }
										: { ...a };
								}),
						  }
						: { ...q };
				}),
			};
		}
		case SET_REGION:
			return {
				...state,
				questions: state.questions.map((q) => {
					if (q.question === action.payload.questionName) {
						return {
							...q,
							answers: q.answers.map((a) => {
								if (a.name === 'common') {
									return {
										...a,
										amount:
											state.postalCode.area ===
												'Berlin' ||
											state.postalCode.area ===
												'Brandenburg'
												? 1
												: 0,
										required: action.payload.value,
									};
								} else {
									return { ...a };
								}
							}),
						};
					} else {
						return { ...q };
					}
				}),
			};
		case ADD_HOUSE: {
			return {
				...state,
				questions: state.questions.map((q) => {
					return q.question === action.payload.questionName
						? {
								...q,
								answers: [
									...q.answers,
									...rooms.map((r) => {
										if (r.name === 'common') {
											return {
												...r,
												amount:
													state.postalCode.area ===
														'Berlin' ||
													state.postalCode.area ===
														'Brandenburg'
														? 1
														: 0,
												required:
													state.postalCode.area ===
														'Berlin' ||
													state.postalCode.area ===
														'Brandenburg'
														? true
														: false,
												house: state.rentings,
											};
										} else {
											return {
												...r,
												house: state.rentings,
											};
										}
									}),
								],
						  }
						: { ...q };
				}),
			};
		}
		case INCREASE_ROOMS:
			return {
				...state,
				questions: state.questions.map((q) => {
					if (q.question === action.payload.questionName) {
						return {
							...q,
							answers: q.answers.map((a) => {
								if (
									a.name === action.payload.roomName &&
									action.payload.index === a.house
								) {
									return {
										...a,
										// @ts-ignore
										amount: parseInt(a.amount) + 1,
									};
								} else {
									return { ...a };
								}
							}),
						};
					} else {
						return { ...q };
					}
				}),
			};
		case DECREASE_ROOMS:
			return {
				...state,
				questions: state.questions.map((q) => {
					if (q.question === action.payload.questionName) {
						return {
							...q,
							answers: q.answers.map((a) => {
								if (
									a.name === action.payload.roomName &&
									action.payload.index === a.house
								) {
									return {
										...a,
										amount:
											getAlarmNumberForHouse(
												state,
												action.payload.index
											) > 1
												? a.amount > 0
													? a.amount - 1
													: a.amount
												: a.amount,
									};
								} else {
									return { ...a };
								}
							}),
						};
					} else {
						return { ...q };
					}
				}),
			};
		default:
			return state;
	}
};

export default appData;
