import tsPlugin from "@typescript-eslint/eslint-plugin"
import tsParser from "@typescript-eslint/parser"
import react from "eslint-plugin-react"
import reactHooks from "eslint-plugin-react-hooks"
import importPlugin from "eslint-plugin-import"
import jsxA11y from "eslint-plugin-jsx-a11y"
import comments from "@eslint-community/eslint-plugin-eslint-comments/configs"
import prettierRecommended from "eslint-plugin-prettier/recommended"

export default [
  {
    ignores: [
      "dist/**",
      "node_modules/**",
      "**/*.json",
      "webpack.config.js",
      "eslint.config.mjs",
      ".prettierrc.js",
    ],
  },
  react.configs.flat.recommended,
  reactHooks.configs.flat["recommended-latest"],
  importPlugin.flatConfigs.errors,
  importPlugin.flatConfigs.warnings,
  importPlugin.flatConfigs.typescript,
  jsxA11y.flatConfigs.recommended,
  comments.recommended,
  prettierRecommended,
  {
    files: ["**/*.{js,jsx,ts,tsx}"],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 2020,
        sourceType: "module",
      },
    },
    settings: {
      react: {
        version: "detect",
      },
    },
    plugins: {
      "@typescript-eslint": tsPlugin,
    },
    rules: {
      ...tsPlugin.configs.recommended.rules,
      "no-unused-vars": "off",
      "@typescript-eslint/no-unused-vars": ["error"],
      "@typescript-eslint/no-var-requires": "off",
      // webpack/tsconfig aliases + packages with "exports" maps the node resolver can't read
      "import/no-unresolved": ["error", { ignore: ["^@img/", "^@fonts/", "^@mui/icons-material/"] }],
      "react/prop-types": "off",
      "react/jsx-uses-react": "off",
      "react/react-in-jsx-scope": "off",
      "@typescript-eslint/explicit-module-boundary-types": "off",
    },
  },
]
