import React from 'react';
import { useSelector } from 'react-redux';
import { AppReduxStoreProps } from '../../redux/reducers/App';
export interface LayoutProps extends React.HTMLProps<HTMLDivElement> {
	children: React.ReactNode;
}

const Layout = ({ children, className = '', ...props }: LayoutProps) => {
	const currentAppStep = useSelector(
		(state: AppReduxStoreProps) => state.appData.step
	);

	return (
		<>
			<main className={className} {...props}>
				{children}
				{currentAppStep < 3 && (
					<p className="tw-font-size-footer-phone">
						*Bitte beachten Sie, dass nur Aufträge zur Ausstattung
						von kompletten Liegenschaften bearbeitet werden können.
					</p>
				)}
			</main>
		</>
	);
};

export default Layout;
