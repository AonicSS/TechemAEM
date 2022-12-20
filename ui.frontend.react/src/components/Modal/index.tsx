import classNames from 'classnames';
import React from 'react';
import { useIntl } from 'react-intl';

import { useSelector } from 'react-redux';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import Translate from '../../utils/translate';

import Button from '../Button';
import Pricing from '../Form/Pricing';
import './Modal.css';

const Modal = () => {
	const intl = useIntl();

	const currentAppStep = useSelector(
		(state: AppReduxStoreProps) => state.appData.step
	);

	const showModal = useSelector(
		(state: AppReduxStoreProps) => state.appData.showModal
	);

	return (
		<div className="tw-container">
			{showModal ? (
				<div className="rwm-overlay">
					<div
						className={classNames(
							'rwm-overlay__container',
							{ 'tw-overlay-size-1': currentAppStep === 4 },
							{ 'tw-overlay-size-2': currentAppStep === 1 },
							{ 'tw-overlay-size-5': currentAppStep === 3 },
							{ 'tw-overlay-size-3': currentAppStep === 5 }
						)}
					>
						<div className="rwm-overlay__btn-container-close">
							<Button style="CLOSE" />
						</div>
						{currentAppStep === 4 ||
							(currentAppStep === 1 && (
								<>
									<div className="rwm-overlay__headline-container">
										<h1 className="rwm-overlay__headline">
											{Translate(
												intl,
												`overlay.${currentAppStep}.headline`
											)}
										</h1>
									</div>
									<div className="rwm-overlay__body-container">
										<p className="rwm-overlay__body">
											{Translate(
												intl,
												`overlay.${currentAppStep}.body.0`
											)}
										</p>
									</div>
								</>
							))}
						{currentAppStep === 3 && (
							<>
								<div className="rwm-overlay__headline-container">
									<h1 className="rwm-overlay__headline">
										{Translate(
											intl,
											`overlay.${currentAppStep}.headline.0`
										)}
									</h1>
								</div>
								<div className="rwm-overlay__body-container">
									<p className="rwm-overlay__body">
										{Translate(
											intl,
											`overlay.${currentAppStep}.body.0`
										)}
									</p>
								</div>
								<div className="rwm-overlay__headline-container tw-mt-6">
									<h1 className="rwm-overlay__headline">
										{Translate(
											intl,
											`overlay.${currentAppStep}.headline.1`
										)}
									</h1>
								</div>
								<div className="rwm-overlay__body-container">
									<p className="rwm-overlay__body">
										{Translate(
											intl,
											`overlay.${currentAppStep}.body.1`
										)}
									</p>
								</div>
							</>
						)}
						{currentAppStep === 5 ||
							(currentAppStep === 1 && (
								<div className="rwm-overlay__contact-container">
									<p className="rwm-overlay__contact-headline">
										{Translate(
											intl,
											`overlay.${currentAppStep}.contact-headline`
										)}
									</p>
									<p className="rwm-overlay__contact-body">
										{Translate(
											intl,
											`overlay.${currentAppStep}.contact-body`
										)}
									</p>
								</div>
							))}
						{currentAppStep === 2 && (
							<div className="rwm-overlay__body-container">
								<p className="rwm-overlay__body">
									{Translate(
										intl,
										`overlay.${currentAppStep}.body.1`
									)}
								</p>
							</div>
						)}
						{currentAppStep === 2 && (
							<div className="rwm-overlay__btn-container-continue">
								<Button style="CONTINUE" />
							</div>
						)}
						{currentAppStep === 5 && (
							<div>
								<Pricing modal={true} />
							</div>
						)}
					</div>
				</div>
			) : null}
		</div>
	);
};

export default Modal;
