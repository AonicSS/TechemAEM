// @ts-nocheck
import isEmail from 'validator/lib/isEmail';
import isPostalCode from 'validator/lib/isPostalCode';
import germanPostalCode from '../shared/federate_states_germany.json';
import { price, nonUserServices, userServices } from '../shared/constants';

export const validEmail = (email: string): boolean => isEmail(email);

// return True if iinput is 5 digit number and postal code is found else False
export const validPostalCode = (postalCode: string): boolean => {
	const postalCodeObject = getFederalStateArray(postalCode);
	return isPostalCode(postalCode, 'DE') && postalCodeObject.length > 0;
};

// return array if postal code is find in list
const getFederalStateArray = (plz: number) => {
	return germanPostalCode.filter(function (germanPostalCode) {
		return germanPostalCode.plz.toString() === plz;
	});
};

// return array if postal code is find in list
export const getFederalState = (postalCode: string) => {
	const plz = parseInt(postalCode);
	return germanPostalCode.filter(function (germanPostalCode) {
		return germanPostalCode.plz === plz;
	});
};

// return type as a string (slided after [object ...). Like typeof but covers also regex etc.
export const trueTypeOf = (obj: any) => {
	return Object.prototype.toString.call(obj).slice(8, -1).toLowerCase();
};

export const getAlarmNumber = (data) => {
	let alarms = 0;

	data.questions['2'].answers.forEach((element) => {
		if (element.house <= data.maxRentings) {
			alarms = alarms + element.amount;
		}
	});

	return alarms;
};

export const getAlarmNumberForHouse = (data, house) => {
	let alarms = 0;

	data.questions['2'].answers.forEach((element) => {
		if (element.house === house) {
			alarms = alarms + element.amount;
		}
	});

	return alarms;
};

//return price for previous user
export const rentingCostUser = (alarms: number, years: number) => {
	const total = (alarms * price[years] * years) / years / alarms;

	return total;
};

//return price for non user
export const rentingCostNonUser = (
	alarms: number,
	years: number,
	rentings: number
) => {
	const total =
		(alarms * price[years] * years +
			alarms * nonUserServices.registration +
			nonUserServices.instalation +
			nonUserServices.radio * rentings) /
		years /
		alarms;

	return total;
};

//return price for non user
export const serviceCostNonUser = (
	alarms: number,
	years: number,
	service: string
) => {
	const total =
		(alarms * nonUserServices[service] * years +
			nonUserServices.grundpreis +
			(service === 'plus' ? nonUserServices.smart : 0)) /
		years /
		alarms;
	return total;
};

//return price for users
export const serviceCostUser = (
	alarms: number,
	years: number,
	service: string
) => {
	const total =
		(alarms * userServices[service] * years +
			(service === 'plus' ? nonUserServices.smart : 0)) /
		years /
		alarms;
	return total;
};

export const getRentingPrice = (data: any) => {
	if (!data.questions[3].choice) {
		return rentingCostNonUser(
			getAlarmNumber(data),
			data.years,
			data.rentings
		);
	} else {
		return rentingCostUser(getAlarmNumber(data), data.years);
	}
};

export const getServicePrice = (type: string, data: any) => {
	if (!data.questions[3].choice) {
		return serviceCostNonUser(getAlarmNumber(data), data.years, type);
	} else {
		return serviceCostUser(getAlarmNumber(data), data.years, type);
	}
};
