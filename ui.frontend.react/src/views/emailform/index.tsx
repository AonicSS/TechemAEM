import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import classNames from 'classnames';
import Layout from '../../components/Layout';
import Modal from '../../components/Modal';
import {
	getAlarmNumber,
	getRentingPrice,
	getServicePrice,
} from '../../utils/helpers';

const EmailForm = () => {
	const [emailAddress, setEmailAddress] = useState('');
	const [marketing, setMarketing] = useState(false);
	const [success, setSucccess] = useState(false);

	const appData = useSelector((state: AppReduxStoreProps) => state.appData);
	const rentingPrice = getRentingPrice(appData);
	const servicePrice = getServicePrice(
		appData.pricing === 'Standard 360 Adv' ? 'plus' : 'standard',
		appData
	);
	const total = rentingPrice + servicePrice;

	const submitForm = () => {
		const response = {
			constactData: {
				emailAddress,
				marketingAgreement: marketing ? 'Yes' : 'No',
			},
			formData: {
				price: total.toFixed(2),
				alarms: getAlarmNumber(appData),
				houses: appData.rentings,
				years: appData.years,
				code: appData.postalCode.code,
				servicePricing: appData.pricing,
				heatingCustomer: appData.questions[3].choice ? 'Yes' : 'No',
				smokeAlarmCustomer: appData.questions[1].choice ? 'Yes' : 'No',
			},
		};
		const apiAddress: string | undefined =
			process.env.REACT_APP_POWER_AUTOMATE_EMAIL;

		if (!apiAddress) throw 'API address not defined';

		fetch(apiAddress, {
			method: 'POST',
			headers: {
				Accept: 'application/json',
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(response),
		}).then((res) => setSucccess(true));
	};

	return (
		<Layout>
			<Modal />
			<section className="rwm-forms__page-section tw-margin-top">
				<div className="tw-flex tw-flex-col">
					<label className="rwm-form__headline">
						<h1>Preisindikation erhalten</h1>
						<h2 className="tw-font-size-info tw-text-center tw-mt-5">
							Lorem ipsum dolor sit amet, consetetur sadipscing
							elitr, sed diam nonumy eirmod tempor invidunt ut
							labore et dolore magna aliquyam erat, sed diam
							voluptua.
						</h2>
					</label>

					<div className="rwm-form__input-container-large tw-flex tw-flex-col tw-justify-center tw-items-start tw-mt-8">
						<label className="tw-flex tw-font-size-label tw-mb-2 tw-font">
							E-Mail Adresse
						</label>
						<input
							type="email"
							name="postalCode"
							className="rwm-form__input-custom tw-border-2 tw-rounded-md focus:tw-ring-transparent"
							value={emailAddress}
							onChange={(e) => setEmailAddress(e.target.value)}
						/>
					</div>
					<div className="rwm-form__input-container-large tw-flex tw-flex-row tw-justify-start tw-items-start tw-mt-8">
						<div className="round">
							<input
								type="checkbox"
								id="checkbox"
								defaultChecked={marketing}
								onChange={() => setMarketing(!marketing)}
							/>
							<label htmlFor="checkbox"></label>
						</div>
						<p className="tw-font-size-label-small tw-pl-6">
							Einwilligung zur Marketing – Lorem ipsum dolor sit
							amet, consetetur sadipscing elitr, sed diam nonumy
							eirmod tempor invidunt ut labore et dolore magna
						</p>
					</div>
					<div className="tw-flex tw-justify-center tw-items-center tw-mt-10">
						<button
							onClick={() => submitForm()}
							className={classNames(
								'rwn-btn-continue',
								'rwm-button--primary'
							)}
						>
							Preisindikation erhalten
						</button>
					</div>
				</div>
			</section>
		</Layout>
	);
};

export default EmailForm;
