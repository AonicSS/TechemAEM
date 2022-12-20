import React, { useCallback } from 'react';
import { useIntl } from 'react-intl';
import Translate from '../../utils/translate';
import { useDispatch, useSelector } from 'react-redux';
import { SET_YEARS } from '../../redux/actions/App';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import Pricing from './Pricing';

const Six = () => {
	const dispatch = useDispatch();
	const intl = useIntl();

	const handleClick = useCallback((value: any) => {
		dispatch({
			type: SET_YEARS,
			payload: {
				years: value,
			},
		});
	}, []);

	const years = useSelector(
		(state: AppReduxStoreProps) => state.appData.years
	);

	return (
		<div className="tw-flex tw-flex-col">
			<label>
				<h1 className="rwm-form__headline">
					{Translate(intl, `questions.4.question`)}
				</h1>
			</label>

			<fieldset className="rwm-radio__container-years tw-mb-14 tw-mt-10 lg:tw-mt-0 xl:tw-mt-0">
				<div className="tw-flex tw-flex-col tw-justify-center tw-items-center">
					<div
						className="rwm-radio__container-select tw-container-radio"
						onClick={() => handleClick(5)}
					>
						<label htmlFor="yes" className="rwm-radio__label">
							5 Jahre
						</label>
						<div className="rwm-form__container-input">
							<input
								onClick={() => handleClick(5)}
								type="radio"
								value="5"
								checked={years === 5}
								className="tw-border-1 tw-border-btnColorDisabled focus:tw-ring-transparent tw-text-white tw-h-5 tw-w-5"
							/>
						</div>
					</div>
				</div>
				<div className="tw-flex tw-flex-col tw-justify-center tw-items-center">
					<div
						className="rwm-radio__container-select tw-container-radio"
						onClick={() => handleClick(8)}
					>
						<label htmlFor="yes" className="rwm-radio__label">
							8 Jahre
						</label>
						<div className="rwm-form__container-input">
							<input
								onClick={() => handleClick(8)}
								type="radio"
								value="8"
								checked={years === 8}
								className="tw-border-1 tw-border-btnColorDisabled focus:tw-ring-transparent tw-text-white tw-h-5 tw-w-5"
							/>
						</div>
					</div>
				</div>
				<div className="tw-flex tw-flex-col tw-justify-center tw-items-center">
					<div
						className="rwm-radio__container-select tw-container-radio"
						onClick={() => handleClick(10)}
					>
						<label htmlFor="yes" className="rwm-radio__label">
							10 Jahre
						</label>
						<div className="rwm-form__container-input">
							<input
								onClick={() => handleClick(10)}
								type="radio"
								value="10"
								checked={years === 10}
								className="tw-border-1 tw-border-btnColorDisabled focus:tw-ring-transparent tw-text-white tw-h-5 tw-w-5"
							/>
						</div>
					</div>
				</div>
			</fieldset>
			<Pricing />
			{/* <Button text="Alle Leistungen der Service Pakete" style={'LINK'} /> */}
		</div>
	);
};

export default Six;
