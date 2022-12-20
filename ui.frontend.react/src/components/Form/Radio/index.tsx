import React, { useEffect } from 'react';
import { useIntl } from 'react-intl';
import * as Scroll from 'react-scroll';
import Translate from '../../../utils/translate';
import { useDispatch, useSelector } from 'react-redux';
import { SET_ANSWER } from '../../../redux/actions/App';
import { ReactComponent as Check } from '../../../icons/check.svg';
import { ReactComponent as Decline } from '../../../icons/times.svg';

import './Radio.css';
import { AppReduxStoreProps } from '../../../redux/reducers/App';

const Radio = () => {
	const dispatch = useDispatch();
	const intl = useIntl();
	const scroller = Scroll.scroller;

	const currentAppStep = useSelector(
		(state: AppReduxStoreProps) => state.appData.step
	);

	const questionText = `${Translate(
		intl,
		`questions.${currentAppStep - 1}.question`
	)}`;

	const currentChoice = useSelector(
		(state: AppReduxStoreProps) =>
			state.appData.questions[currentAppStep - 1].choice
	);

	useEffect(() => {
		scroller.scrollTo('myScrollToElement', {
			duration: 1500,
			delay: 100,
			smooth: true,
			offset: -50,
		});
	}, []);

	const handleChange = (value: boolean) => {
		dispatch({
			type: SET_ANSWER,
			payload: {
				questionName: questionText,
				choice: value,
				btnActive: true,
			},
		});
		// if (val && currentAppStep === 2) {
		// 	dispatch({ type: SET_MODAL, payload: { showModal: true } });
		// }
	};

	return (
		<div className="rwm-radio">
			<label className="rwm-form__headline">{questionText}</label>
			<fieldset className="rwm-radio__container lg:tw-mt-16 xl:tw-mt-16">
				<div
					className="rwm-radio__container-select tw-container-radio-first"
					onClick={() => handleChange(true)}
				>
					<div className="rwm-radio__container-icon-check">
						<Check fill="#4c4c4c" />
					</div>
					<label htmlFor="Ja" className="rwm-radio__label tw-mb-1">
						Ja
					</label>
					<div className="rwm-form__container-input">
						<input
							onChange={() => handleChange(true)}
							name="Ja"
							id="Ja"
							type="radio"
							checked={currentChoice === true}
							className="tw-border-1 tw-border-btnColorDisabled focus:tw-ring-transparent tw-text-white tw-h-5 tw-w-5"
						/>
					</div>
				</div>
				<div
					className="rwm-radio__container-select tw-container-radio"
					onClick={() => handleChange(false)}
				>
					<div className="rwm-radio__container-icon-decline">
						<Decline fill="#4c4c4c" />
					</div>
					<label htmlFor="Nein" className="rwm-radio__label tw-mb-1">
						Nein
					</label>
					<div className="rwm-form__container-input">
						<input
							onChange={() => handleChange(false)}
							name="Nein"
							id="Nein"
							type="radio"
							checked={currentChoice === false}
							className="tw-border-1 tw-border-btnColorDisabled focus:tw-ring-transparent tw-text-white tw-h-5 tw-w-5"
						/>
					</div>
				</div>
			</fieldset>
		</div>
	);
};

export default Radio;
