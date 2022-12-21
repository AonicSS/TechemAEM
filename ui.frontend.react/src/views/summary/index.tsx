import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import { useNavigate } from 'react-router-dom';
import classNames from 'classnames';
import { SET_MODAL } from '../../redux/actions/App';
import Layout from '../../components/Layout';
import Modal from '../../components/Modal';
import Button from '../../components/Button';
import {
	getAlarmNumber,
	getRentingPrice,
	getServicePrice,
} from '../../utils/helpers';
import { ReactComponent as Info } from '../../icons/Info.svg';
import * as Scroll from 'react-scroll';
import { trackSummary } from '../../utils/tracking';

const Summary = () => {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const appData = useSelector((state: AppReduxStoreProps) => state.appData);
	const scroller = Scroll.scroller;
	const Element = Scroll.Element;
	useEffect(() => {
		scroller.scrollTo('myScrollToElement', {
			duration: 1500,
			delay: 100,
			smooth: true,
			offset: -100,
		});

		const pricing =
			appData.pricing === 'Standard 360 Adv' ? 'plus' : 'standard';
		trackSummary('summary', pricing);
	}, []);

	const rentingPrice = getRentingPrice(appData);
	const servicePrice = getServicePrice(
		appData.pricing === 'Standard 360 Adv' ? 'plus' : 'standard',
		appData
	);
	const total = rentingPrice + servicePrice;

	return (
		<Layout>
			<Modal />
			<Element name="myScrollToElement"></Element>
			<section className="rwm-calculator__page-section tw-margin-top">
				<div className="tw-flex tw-flex-col">
					<div className="rwm-icon tw-justify-start">
						<div
							onClick={() => navigate('/')}
							className="rwm-arrow tw-ml-5 tw-mt-2"
						></div>
					</div>
					<div className="rwm-form__headline-mobile lg:tw-mt-[-30px] xl:tw-mt-[-30px]">
						<label className="rwm-form__headline tw-text-center">
							<h1 className="rwm-form__headline">
								Ihr Rauchwarnmelder - Angebot
							</h1>
						</label>
					</div>
					<div className="tw-grid tw-grid-cols-1 md:tw-grid-cols-1 lg:tw-grid-cols-3 xl:tw-grid-cols-3 tw-gap-10 xl:tw-gap-14 tw-mt-16">
						<div
							className={classNames(
								'tw-container-summary tw-justify-center tw-items-center tw-pb-6'
							)}
						>
							<div className="tw-container-pricing-headline tw-font-size-pricing-headline">
								Ihre Angaben
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{getAlarmNumber(appData)}
							</div>
							<div className="tw-container-pricing-sublabel tw-font-size-pricing-sublabel">
								Anzahl Rauchwarnmelder
							</div>

							{/* <div className="tw-container-pricing-sublabel tw-font-size-pricing-sublabel">
								Heizkostenabrechnung bei Techem
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{appData.questions[1].choice ? 'Ja' : 'Nein'}
							</div> */}

							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{appData.maxRentings}
							</div>
							<div className="tw-container-pricing-sublabel tw-font-size-pricing-sublabel">
								Wohnungen
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{appData.postalCode.code}
							</div>
							<div className="tw-container-pricing-sublabel tw-font-size-pricing-sublabel">
								Postleitzahl
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{appData.questions[3].choice ? 'Ja' : 'Nein'}
							</div>
							<div className="tw-container-pricing-sublabel tw-font-size-pricing-sublabel">
								Kunde bei Techem
							</div>
						</div>

						<div
							className={classNames(
								'tw-container-summary tw-justify-center tw-items-center tw-pb-6'
							)}
						>
							<div className="tw-container-pricing-headline tw-font-size-pricing-headline">
								Ihr Produktpaket
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{`Gesamtpreis für Gerätemiete & Service`}
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label tw-flex">
								{appData.pricing === 'Standard 360 Adv'
									? 'Plus'
									: ''}
								<Info
									onClick={() =>
										dispatch({
											type: SET_MODAL,
											payload: {
												showModal: true,
											},
										})
									}
									className="rwm-btn-info"
								/>
							</div>
							<div className="tw-container-pricing-label tw-font-size-price-large">
								{total.toFixed(2).toString().replace('.', ',')}{' '}
								€
							</div>
							<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
								pro Gerät / Jahr
							</div>
							<div className="tw-container-pricing-label tw-font-size-pricing-label">
								{appData.years} Jahre
							</div>
							<div className="tw-container-pricing-sublabel tw-font-size-pricing-sublabel">
								Laufzeit
							</div>
						</div>
						<div
							className={classNames(
								'tw-container-summary tw-justify-center tw-items-center tw-pb-6'
							)}
						>
							<div className="tw-container-pricing-headline tw-font-size-pricing-headline">
								Ihr Angebot ist nur noch einen Schritt entfernt!
							</div>
							<div className="tw-flex tw-flex-col tw-content-left tw-items-start tw-align-items-start tw-pt-3 tw-pl-6">
								<Button
									text="Angebot anfordern"
									style="FORM-LINK-PRIMARY"
									link="contactform"
								/>
								{/* <Button
									text="Erinnerung erhalten"
									style="FORM-LINK"
									link="emailform"
								/> */}
								<Button
									text="Erinnerung erhalten"
									style="FORM-LINK"
									link="reminder"
								/>
							</div>
						</div>
					</div>
				</div>
			</section>
		</Layout>
	);
};

export default Summary;
