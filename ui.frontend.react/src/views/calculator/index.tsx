import React from 'react';
import { useSelector } from 'react-redux';
import { AppReduxStoreProps } from '../../redux/reducers/App';
import Layout from '../../components/Layout';
import Stepper from '../../components/Stepper';
import Modal from '../../components/Modal';
import Form from '../../components/Form';
import Button from '../../components/Button';
import * as Scroll from 'react-scroll';

const Calculator = () => {
	const currentAppStep = useSelector(
		(state: AppReduxStoreProps) => state.appData.step
	);
	const Element = Scroll.Element;
	return (
		<Layout>
			<Modal />
			<Element name="myScrollToElement"></Element>
			<section className="rwm-calculator__page-section tw-margin-top">
				<Stepper />
			</section>
			<section className="rwm-calculator__page-section tw-mt-8">
				<Form />
			</section>
			{currentAppStep !== 5 ? (
				<section className="rwm-calculator__page-section tw-mt-14">
					<Button style="NEXT" />
				</section>
			) : null}
		</Layout>
	);
};

export default Calculator;
