import classNames from 'classnames';
import React from 'react';
import Button from '../Button';
import { useSelector } from 'react-redux';
import './Pricing.css';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import { getRentingPrice, getServicePrice } from '../../utils/helpers';

const pricing = [
	{
		name: 'Standard',
		position: 'tw-container-pricing-1',
		alarmFeatures: [
			'Modernste Geräte in zertifizierter Qualität',
			'Mindestens 10 Jahre Lebensdauer',
			'Fachmännische Planung & Montage',
		],
		serviceFeatures: [
			'Automatische Funk-Ferninspektion 1x/Jahr',
			'Speicherung der Prüfprotokolle im Kundenportal',
			'Online-Statusanzeige - 1x/Jahr aktualisiert',
			'Kostenlose 24/7-Hotline',
			'Störungsbehebung bei Bedarf & automatisiert 1x/Jahr',
		],
		buttonStyle: 'SECONDARY',
		text: 'Basis Paket wählen',
		type: 'standard',
		cheapest: false,
	},
	// {
	// 	name: 'Standard 360',
	// 	position: 'tw-container-pricing-2',
	// 	features: [
	// 		'Lorem text 1 ipsum dolor sit amet, consectetur adipiscing',
	// 		'Lorem text 2 ipsum dolor sit amet, consectetur adipiscing',
	// 		'Lorem text 3 ipsum dolor sit amet, consectetur adipiscing',
	// 		'Lorem text 4 ipsum dolor sit amet, consectetur adipiscing',
	// 		'Lorem text 5 ipsum dolor sit amet, consectetur adipiscing',
	// 	],
	// 	buttonStyle: 'PRIMARY',
	// 	text: 'Standard 360 wählen',
	// },
	{
		name: 'Standard 360 Adv',
		position: 'tw-container-pricing-3',
		alarmFeatures: [
			'Modernste Geräte in zertifizierter Qualität',
			'Mindestens 10 Jahre Lebensdauer',
			'Fachmännische Planung & Montage',
		],
		serviceFeatures: [
			'Automatische Funk-Ferninspektion 2x/Monat',
			'Speicherung der Prüfprotokolle im Kundenportal',
			'Online-Statusanzeige - bis zu 2x/Monat aktualisiert',
			'Kostenlose 24/7-Hotline',
			'Störungsbehebung bei Bedarf & automatisiert 2x/Monat aktualisiert',
			'Inklusive Smart Reader',
			'Lückenlose Transparenz & minimiertes Haftungsrisiko',
		],
		buttonStyle: 'PRIMARY',
		text: 'Plus Paket wählen',
		type: 'plus',
		cheapest: true,
	},
];
export interface PricingProps extends React.HTMLProps<HTMLDivElement> {
	modal?: boolean;
}

const Pricing = ({ modal }: PricingProps) => {
	const appData = useSelector((state: AppReduxStoreProps) => state.appData);

	return (
		<div className="tw-pb-12">
			<div className="tw-grid tw-align-center tw-grid-cols-1 lg:tw-grid-cols-2 xl:tw-grid-cols-2 tw-gap-[52px] xl:tw-gap-[50px]">
				{pricing.map((p) => {
					const rentingPrice = getRentingPrice(appData);
					const servicePrice = getServicePrice(p.type, appData);

					const total = rentingPrice + servicePrice;

					if (!modal) {
						return (
							<div
								key={p.name}
								className={classNames(
									'tw-container-pricing tw-justify-center tw-items-center tw-border-2 tw-border-grey tw-pb-6',
									`${p.position}`
								)}
							>
								{p.cheapest && (
									<div className="rwm-best-price">
										Techem Empfehlung
									</div>
								)}
								<div className="tw-container-pricing-headline tw-font-size-pricing-headline">
									{`Gesamtpreis für Gerätemiete & Service`}{' '}
									{p.name === 'Standard' ? '' : 'Plus'}
								</div>
								<div className="tw-container-pricing-label tw-font-size-price-large">
									{`Nur ${total
										.toFixed(2)
										.toString()
										.replace('.', ',')} 
									€`}
								</div>
								<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
									pro Gerät / Jahr
								</div>
								<div className="tw-container-pricing-label tw-font-size-pricing-label">
									Rauchwarnmelder-Miete
								</div>
								<div className="tw-container-pricing-label tw-font-size-price-small">
									{`${rentingPrice
										.toFixed(2)
										.toString()
										.replace('.', ',')} €`}
								</div>
								<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
									pro Gerät / Jahr
								</div>
								{p.alarmFeatures.map((f) => {
									// if (isModalVisible || index < 3) {
									return (
										<ul
											key={f}
											className="tw-container-pricing-list tw-list-disc tw-ml-5"
										>
											<li className="tw-font-size-pricing-body">
												<span>{f}</span>
											</li>
										</ul>
									);
									// } else {
									// 	return null;
									// }
								})}
								<div className="tw-container-pricing-label tw-font-size-pricing-label">
									Rauchwarnmelder-Service{' '}
									{p.name === 'Standard' ? '' : 'Plus'}
								</div>
								<div className="tw-container-pricing-label tw-font-size-price-small">
									{`Service ${servicePrice
										.toFixed(2)
										.toString()
										.replace('.', ',')} €`}
								</div>
								<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
									pro Gerät / Jahr
								</div>
								{p.serviceFeatures.map((f) => {
									// if (isModalVisible || index < 3) {
									return (
										<ul
											key={f}
											className="tw-container-pricing-list tw-list-disc tw-ml-5"
										>
											<li className="tw-font-size-pricing-body">
												<span>{f}</span>
											</li>
										</ul>
									);
									// } else {
									// 	return null;
									// }
								})}
								<div className="tw-flex tw-justify-center">
									<Button
										text={p.text}
										style={p.buttonStyle}
										pricing={p.name}
									/>
								</div>
							</div>
						);
					} else {
						if (p.name === appData.pricing) {
							return (
								<div
									key={p.name}
									className={classNames(
										'tw-container-pricing-nobg tw-justify-center tw-items-center tw-border-2 tw-border-grey tw-pb-6'
									)}
								>
									<div className="tw-container-pricing-modal">
										<div className="tw-container-pricing-headline-modal tw-font-size-pricing-headline">
											{`Gesamtpreis für Gerätemiete & Service`}{' '}
											{p.name === 'Standard'
												? ''
												: 'Plus'}
										</div>
										<div className="tw-container-pricing-label tw-font-size-price-large">
											{total
												.toFixed(2)
												.toString()
												.replace('.', ',')}{' '}
											€
										</div>
										<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
											pro Gerät / Jahr
										</div>
										<div className="tw-container-pricing-label tw-font-size-pricing-label">
											Rauchwarnmelder-Miete
										</div>
										<div className="tw-container-pricing-label tw-font-size-price-small">
											{`Nur ${rentingPrice
												.toFixed(2)
												.toString()
												.replace('.', ',')} €`}
										</div>
										<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
											pro Gerät / Jahr
										</div>
										{p.alarmFeatures.map((f) => {
											return (
												<ul
													key={f}
													className="tw-container-pricing-list tw-list-disc tw-ml-5"
												>
													<li className="tw-font-size-pricing-body">
														<span>{f}</span>
													</li>
												</ul>
											);
										})}
										<div className="tw-container-pricing-label tw-font-size-pricing-label">
											Rauchwarnmelder-Service{' '}
											{p.name === 'Standard'
												? ''
												: 'Plus'}
										</div>
										<div className="tw-container-pricing-label tw-font-size-price-small">
											{`Service ${servicePrice
												.toFixed(2)
												.toString()
												.replace('.', ',')} €`}
										</div>
										<div className="tw-container-pricing-sublabel tw-font-size-price-sublabel">
											pro Gerät / Jahr
										</div>
										{p.serviceFeatures.map((f) => {
											return (
												<ul
													key={f}
													className="tw-container-pricing-list tw-list-disc tw-ml-5"
												>
													<li className="tw-font-size-pricing-body">
														<span>{f}</span>
													</li>
												</ul>
											);
										})}
									</div>
								</div>
							);
						} else {
							return null;
						}
					}
				})}
			</div>
		</div>
	);
};

export default Pricing;
