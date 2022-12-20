import React from 'react';
import { Route, Routes, HashRouter } from 'react-router-dom';
import Calculator from './views/calculator';
import EmailForm from './views/emailform';
import ContactForm from './views/contactform';
import ReminderForm from './views/reminderform';
import Summary from './views/summary';

import './styles/index.css';

function App() {
	return (
		<HashRouter basename={'/calculator'}>
			<Routes>
				<Route path="/" element={<Calculator />} />
				<Route path="/summary" element={<Summary />} />
				<Route path="/reminder" element={<ReminderForm />} />
				<Route path="/emailform" element={<EmailForm />} />
				<Route path="/contactform" element={<ContactForm />} />
				<Route path="*" element={<Calculator />} />
			</Routes>
		</HashRouter>
	);
}

export default App;
