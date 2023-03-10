module.exports = {
	env: {
		browser: true,
		es6: true,
	},
	root: true,
	parser: '@typescript-eslint/parser',
	extends: [
		'plugin:@typescript-eslint/recommended', // uses typescript-specific linting rules
		'plugin:react/recommended', // uses react-specific linting rules
		'plugin:prettier/recommended', // enables eslint-plugin-prettier and eslint-config-prettier
		'prettier', // disables react-specific linting rules that conflict with prettier
	],
	globals: {
		Atomics: 'readonly',
		SharedArrayBuffer: 'readonly',
	},
	parserOptions: {
		ecmaFeatures: {
			jsx: true,
		},
		ecmaVersion: 2018,
		sourceType: 'module',
	},
	plugins: ['react', '@typescript-eslint'],
	rules: {
		'react/jsx-filename-extension': [
			1,
			{ extensions: ['.js', '.jsx', '.tsx'] },
		],
		'react/prop-types': 1, // We use TS. Do not need proptypes validation. >:#
		'@typescript-eslint/no-explicit-any': 'off',
		'@typescript-eslint/explicit-module-boundary-types': 'off',
		'react/jsx-no-target-blank': 'warn',
		'prettier/prettier': 'warn',
		'@typescript-eslint/ban-ts-comment': 'warn',

		// '@typescript-eslint/ban-types': [
		// 	'error',
		// 	{
		// 		extendDefaults: true,
		// 		types: {
		// 			'{}': false,
		// 		},
		// 	},
		// ],
	},
	settings: {
		react: {
			createClass: 'createReactClass', // Regex for Component Factory to use,
			// default to "createReactClass"
			pragma: 'React', // Pragma to use, default to "React"
			version: 'detect', // React version. "detect" automatically picks the version you have installed.
			// You can also use `16.0`, `16.3`, etc, if you want to override the detected value.
			// default to latest and warns if missing
			// It will default to "detect" in the future
			flowVersion: '0.53', // Flow version
		},
		propWrapperFunctions: [
			// The names of any function used to wrap propTypes, e.g. `forbidExtraProps`. If this isn't set, any propTypes wrapped in a function will be skipped.
			'forbidExtraProps',
			{ property: 'freeze', object: 'Object' },
			{ property: 'myFavoriteWrapper' },
		],
		linkComponents: [
			// Components used as alternatives to <a> for linking, eg. <Link to={ url } />
			'Hyperlink',
			{ name: 'Link', linkAttribute: 'to' },
		],
	},
};
