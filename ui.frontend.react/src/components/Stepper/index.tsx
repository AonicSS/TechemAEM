import React, { useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { SET_APP_STEP } from '../../redux/actions/App';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import classnames from 'classnames';
import Button from '../../components/Button';

import './Stepper.css';

const Stepper = () => {
	const dispatch = useDispatch();

	const currentAppStep = useSelector(
		(state: AppReduxStoreProps) => state.appData.step
	);

	const maxSteps = useSelector(
		(state: AppReduxStoreProps) => state.appData.maxSteps
	);

	const showModal = useSelector(
		(state: AppReduxStoreProps) => state.appData.showModal
	);

	const setAppStep = (step: number) => {
		step < currentAppStep
			? dispatch({ type: SET_APP_STEP, payload: { step: step } })
			: currentAppStep;
	};

	return (
		<div className="rwm-stepper">
			{currentAppStep !== 1 && !showModal ? (
				<Button style="PREVIOUS" />
			) : (
				<div className="rwm-icon--hidden"></div>
			)}
			{Array(maxSteps)
				.fill(0)
				.map((_, key) => (
					<button
						key={key}
						onClick={() => setAppStep(key + 1)}
						className={classnames('rwm-stepper__rectangle', {
							'rwm-stepper__rectangle--active':
								currentAppStep >= key + 1,
							'rwm-stepper__rectangle--disabled':
								currentAppStep < key + 1,
						})}
					></button>
				))}
		</div>
	);
};

export default Stepper;
