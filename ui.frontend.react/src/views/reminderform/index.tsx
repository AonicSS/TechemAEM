import React, { useEffect } from 'react';
import Layout from '../../components/Layout';

declare global {
	interface Window {
		MktoForms2: any;
		siteDataLayer: any;
	}
}

const ReminderForm = () => {
	useEffect(() => {
		const MktoForms = window.MktoForms2;
		MktoForms.loadForm('//www2.techem.com', '549-BHO-641', 1463);
	}, []);

	return (
		<Layout>
			<section className="rwm-marketo__page-section tw-margin-top">
				<h1 className="rwm-radio__headline">Erinnerung erhalten</h1>
				<form id="mktoForm_1463"></form>
			</section>
		</Layout>
	);
};

export default ReminderForm;
