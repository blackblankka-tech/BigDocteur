---
name: Clinical Precision Next
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#414754'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#717786'
  outline-variant: '#c1c6d7'
  surface-tint: '#005bc0'
  primary: '#0059bb'
  on-primary: '#ffffff'
  primary-container: '#0070ea'
  on-primary-container: '#fefcff'
  inverse-primary: '#adc7ff'
  secondary: '#505f76'
  on-secondary: '#ffffff'
  secondary-container: '#d0e1fb'
  on-secondary-container: '#54647a'
  tertiary: '#00628d'
  on-tertiary: '#ffffff'
  tertiary-container: '#007cb1'
  on-tertiary-container: '#fcfcff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d8e2ff'
  primary-fixed-dim: '#adc7ff'
  on-primary-fixed: '#001a41'
  on-primary-fixed-variant: '#004493'
  secondary-fixed: '#d3e4fe'
  secondary-fixed-dim: '#b7c8e1'
  on-secondary-fixed: '#0b1c30'
  on-secondary-fixed-variant: '#38485d'
  tertiary-fixed: '#c9e6ff'
  tertiary-fixed-dim: '#89ceff'
  on-tertiary-fixed: '#001e2f'
  on-tertiary-fixed-variant: '#004c6e'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.3'
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.5'
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 48px
  xxl: 80px
  container-max: 1280px
  gutter: 24px
---

## Brand & Style

The design system evolves the "Clinical Precision" narrative from a traditional medical interface into a high-end MedTech experience. It balances institutional reliability with the agility of a modern technology platform. The aesthetic is rooted in **Corporate Modernism** with a heavy influence of **Minimalism**, prioritizing clarity, sterility (in the professional sense), and effortless navigation.

The system targets healthcare professionals and patients who require complex data to feel manageable and urgent actions to feel calm. By utilizing expansive whitespace and a vibrant primary accent, the UI evokes a sense of "Clean Innovation"—an environment where technology enhances human care without creating friction.

## Colors

The palette is centered around **Electric Blue**, a high-vibrancy primary color that signals modernization and digital fluency. This is supported by a sophisticated range of slate neutrals that maintain the "clinical" foundation without feeling cold.

- **Primary Actions:** Utilize the Electric Blue, occasionally enhanced by a subtle diagonal gradient to add depth to high-priority touchpoints.
- **Surface Treatments:** The background remains a crisp, pure white. Secondary surfaces and containers use very light slate washes to differentiate content areas without the need for heavy lines.
- **Accents:** Tertiary sky blues are reserved for informational callouts or status indicators that require a softer touch than the primary action color.

## Typography

This design system employs a dual-font strategy to balance personality with utility. **Plus Jakarta Sans** is used for headlines to provide a friendly, modern tech-forward feel with its slightly wider apertures. **Inter** is utilized for all body and functional text to ensure maximum legibility and a systematic, precise appearance at small sizes.

Large display type should use tighter letter-spacing to maintain a "designed" feel. Labels and captions should use Inter Bold with slight tracking increases to maintain readability in data-heavy clinical views.

## Layout & Spacing

The layout philosophy is built on a **Fluid Grid** with generous internal "breathing room." We utilize an 8pt spatial system, but double the standard margins for a high-end feel.

- **Desktop:** 12-column grid, 24px gutters, and 80px side margins to center the eye on critical data.
- **Tablet:** 8-column grid, 16px gutters, 40px margins.
- **Mobile:** 4-column grid, 16px gutters, 16px margins.

Vertical rhythm should be loose. Use `xl` (48px) and `xxl` (80px) spacing between major sections to prevent the "cluttered dashboard" look common in older medical software.

## Elevation & Depth

To maintain a crisp aesthetic, depth is achieved through **Tonal Layers** and **Ambient Shadows** rather than heavy borders.

- **Surface Tiers:** Use a background color of #FFFFFF. Primary containers (cards) should use a subtle 1px border in #E2E8F0.
- **Shadows:** Use extremely soft, low-opacity shadows (Blur: 20px, Y-Offset: 4px, Color: rgba(0, 123, 255, 0.05)). The slight blue tint in the shadow maintains the brand connection and feels cleaner than a grey shadow.
- **Active States:** Elevate elements slightly on hover by increasing shadow spread and removing the border treatment, creating a sense of "floating" precision.

## Shapes

The shape language is defined by a consistent **16px radius (rounded-lg)** for all primary containers and buttons. This significant roundness softens the serious nature of medical data, making the software feel more approachable and modern.

- **Standard Elements:** 8px (rounded-md) for smaller inputs and utility icons.
- **Main Cards/Modals:** 16px (rounded-lg) for structural components.
- **Pills:** Full radius for status chips and tags to contrast against the structured grid.

## Components

- **Buttons:** Primary buttons use the Electric Blue gradient with white text. Secondary buttons use a transparent background with a 1px #E2E8F0 border. All buttons carry the 16px corner radius.
- **Cards:** Cards should have no shadow by default, instead using a subtle #F1F5F9 background fill or a #E2E8F0 border. Shadows are reserved for active or "in-flight" states.
- **Input Fields:** Use a 16px radius. The border should be #E2E8F0, changing to #007BFF on focus. Use Inter Sm (14px) for placeholder text.
- **Chips/Status:** These are pill-shaped (full radius). Use a 10% opacity version of the status color (e.g., 10% Blue for "Processing") with high-contrast text.
- **Lists:** Use generous vertical padding (16px - 24px) between list items. Use thin, 1px horizontal dividers in #F1F5F9 rather than boxed list items to maintain a lightweight feel.
- **Data Visualization:** Charts should use the primary Electric Blue as the lead color, supported by a palette of soft teals and purples for secondary data points.