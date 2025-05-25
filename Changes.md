# Guide to Customizing Graphics and Texts in LyriaTelecom App

This document provides guidance on where to find and how to modify various visual elements and text content within the LyriaTelecom application.

## 1. Themes and Colors

The application uses a theming system that allows for different color schemes (e.g., Orange, Blue, Green) and supports day (light) and night (dark) modes.

### 1.1. Core Color Palette
*   **Location**: `app/src/main/res/values/colors.xml`
*   **Description**: This file defines the base named colors used throughout the application (e.g., `orange_main_500`, `blue_main_500`, `gray_main2_100`). Modify these hex values to change the fundamental colors.

### 1.2. Theme Definitions
*   **Day Themes**: `app/src/main/res/values/themes.xml`
*   **Night Themes**: `app/src/main/res/values-night/themes.xml`
*   **Description**: These files define themes (e.g., `Theme.LyriaTelecom`, `Theme.LyriaTelecomBlue`). Themes map semantic color attributes (like `colorPrimary`, `colorSurface`, or custom ones like `color_main1_500`, `color_text`) to the concrete colors defined in `colors.xml`.
    *   The base theme is `Theme.LyriaTelecom` (which defaults to an orange accent).
    *   Variants like `Theme.LyriaTelecomBlue` override attributes like `color_main1_500` to use different color families.
    *   Ensure consistency between day and night theme definitions for a cohesive user experience. Theme names in `values-night/themes.xml` should mirror those in `values/themes.xml` (e.g., `Theme.LyriaTelecom` for both).

### 1.3. Enabling User Theme Color Selection
*   **File**: `app/src/main/java/org/linphone/core/CorePreferences.kt`
*   **Preference**: `changeMainColorAllowed`
*   **Description**: By default, this is `false`. If set to `true`, a color picker will be enabled in the app's settings (`SettingsFragment.kt`), allowing users to choose their preferred accent color (Orange, Blue, etc.).
*   **File**: `app/src/main/assets/linphonerc_factory` (or `linphonerc_default` before app first run)
*   **Config**: `[ui]` section, `change_main_color_allowed=0` (set to `1` to enable).
*   **Note**: The application also supports Material You dynamic theming via `DynamicColors.applyToActivitiesIfAvailable(this)` in `LinphoneApplication.kt`. If `changeMainColorAllowed` is false and Material You is active on the user's device, wallpaper-based colors may be used. Otherwise, the theme specified in `AndroidManifest.xml` (defaulting to `Theme.LyriaTelecom` - Orange) will be used.

### 1.4. Applying a Specific Theme by Default
*   To change the default theme for the entire application (if not using user selection or Material You override):
    1.  Modify `android:theme` attribute in the `<application>` tag in `app/src/main/AndroidManifest.xml`. For example, to make Blue the default: `android:theme="@style/Theme.LyriaTelecomBlue"`.
    2.  Activities can also have their own themes specified with `android:theme` in the manifest.

## 2. Logos and Icons

Logos and icons can be vector drawables (XML) or bitmap images (PNGs). For theme adaptability, vector drawables are preferred, using theme attributes for colors.

### 2.1. App Launcher Icon
*   **Manifest Reference**: `android:icon` and `android:roundIcon` in `<application>` tag of `app/src/main/AndroidManifest.xml` (currently points to `@drawable/icono`).
*   **File**: `app/src/main/res/drawable/icono.xml`. This is a vector drawable.
*   **Customization**:
    *   To change the graphic, edit the path data in this XML.
    *   To change its color (if it needs to be theme-aware *when used inside the app*): Modify the `fillColor` attribute to reference a theme attribute (e.g., `?attr/color_main1_500`). For the launcher icon itself, static brand colors are common.

### 2.2. Splash Screen Logo
*   **Theme Reference**: `windowSplashScreenAnimatedIcon` item in `AppSplashScreenTheme` within:
    *   `app/src/main/res/values/themes.xml` (for day mode, currently `@drawable/ic_lyria_logo_color`)
    *   `app/src/main/res/values-night/themes.xml` (for night mode, currently `@drawable/linphone_splashscreen`)
*   **Files**:
    *   `app/src/main/res/drawable/ic_lyria_logo_color.xml` (vector drawable)
    *   `app/src/main/res/drawable/linphone_splashscreen.xml` (vector drawable)
*   **Customization**: Edit these vector drawables. For theme adaptability (recommended), ensure `fillColor` attributes use theme attributes (e.g., `?attr/color_main1_500` for accented parts, `?attr/colorOnSurface` or `?attr/colorSurface` for neutral parts).

### 2.3. Assistant Logo
*   **File**: `app/src/main/res/drawable/assistant_logo.xml` (vector drawable)
*   **Usage**: Likely used in fragments loaded by `AssistantActivity` (via `assistant_nav_graph`).
*   **Customization**: Edit this vector drawable. For theme adaptability, ensure `fillColor` attributes use theme attributes.

### 2.4. Other Icons and Images
*   **General Location**: `app/src/main/res/drawable/` (for XML drawables or general bitmaps) and `app/src/main/res/mipmap-*dpi/` (primarily for launcher icons, but sometimes other high-res bitmaps).
*   **Customization**:
    *   Replace PNG files with your desired assets, ensuring correct naming and density versions if applicable.
    *   For XML vector drawables, edit their path data or colors. Use theme attributes (`?attr/...`) for `fillColor` or `tint` to make them adapt to the current theme. For example, an icon in a layout XML:
        ```xml
        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/your_icon"
            app:tint="?attr/colorPrimary" />
        ```

## 3. Texts (Strings)

### 3.1. Application Name
*   **File**: `app/src/main/res/values/strings.xml`
*   **String Name**: `app_name`
*   **Customization**: Change the value of this string to rename the application.

### 3.2. General UI Texts
*   **Primary Location**: `app/src/main/res/values/strings.xml`
*   **Localized Texts**: `app/src/main/res/values-fr/strings.xml`, `app/src/main/res/values-es/strings.xml`, etc.
*   **Description**: This file contains most of the user-visible text. Find the string by its name (e.g., `settings_title`, `call_button_label`) and modify its value. For internationalization, provide translations in the respective `values-*` folders.

### 3.3. Text Appearance (Color, Size, Style)
*   Text appearance is generally controlled by styles and theme attributes.
*   **Styles**: Defined in `app/src/main/res/values/styles.xml` (if it exists) or directly within `app/src/main/res/values/themes.xml`. Look for `<style>` tags (e.g., `section_header_style`, `default_text_style` found in `account_settings_fragment.xml`).
*   **Theme Attributes**: Layout XMLs often reference theme attributes for text colors (e.g., `android:textColor="?attr/color_text"` or `android:textColor="?attr/color_main1_500"`).
*   **Directly in Layouts**: While less common for general styling, specific texts might have `android:textColor`, `android:textSize`, `android:textStyle` attributes set directly in their layout XML files (e.g., in `app/src/main/res/layout/`).

## 4. Summary of Key Files for Theming and Branding

*   **Colors**: `app/src/main/res/values/colors.xml`
*   **Themes (Day)**: `app/src/main/res/values/themes.xml`
*   **Themes (Night)**: `app/src/main/res/values-night/themes.xml`
*   **Primary Text Strings**: `app/src/main/res/values/strings.xml`
*   **App Launcher Icon**: `app/src/main/res/drawable/icono.xml` (and `AndroidManifest.xml`)
*   **Splash Screen Logos**: `app/src/main/res/drawable/ic_lyria_logo_color.xml`, `app/src/main/res/drawable/linphone_splashscreen.xml`
*   **Other Vector Drawables**: `app/src/main/res/drawable/`
*   **Theme Logic/Preferences**: `app/src/main/java/org/linphone/ui/main/settings/fragment/SettingsFragment.kt`, `app/src/main/java/org/linphone/core/CorePreferences.kt` (especially `changeMainColorAllowed`).
*   **Default RC file**: `app/src/main/assets/linphonerc_factory` (see `[ui]` section for `change_main_color_allowed`).

Remember to test your changes thoroughly in both day and night modes, and across different theme selections if user theme selection is enabled.
