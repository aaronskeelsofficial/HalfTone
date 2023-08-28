# HalfTone
(Input Image, x/y pixel delta, pattern size lower/upper bound, size/location/rotation tolerances, colorized?) -> (Image drawn of other pattern image)

# Purpose
My friend sent me a [video](https://www.instagram.com/p/CwapMK6gOUB/) someone on Instagram posted of their "halftone software which is better than Photoshop". I'm not very involved in the image processing or graphic design world so I didn't formally know exactly what the terminology meant, or what was happening behind the scenes. Intuitively, however, I felt like I could recreate what he was doing not only easily but **better** within a single night. So this is my attempt to do that. My version is better than the Instagram user's because mine allows for colorization, pattern size "fuzz", pattern location "fuzz", and pattern rotation randomization.

# Configurable Variables
- Pattern Size % Upper/Lower Bound (uses linear interpolation bc other interpolation functions looked terrible)
- Pattern Size Randomized Tolerance \[px\]
- Image Sample Grid X/Y Delta \[px\]
- Pattern Location Randomized Tolerance \[px\]
- Pattern Randomized Rotation
- Colorize Pattern

# Tutorial
1. Run "halftone.jar" for b&w input/output images, run "halftone_colorized.jar" for colorful input/output images. Running jars is done via double click. You need Java installed.
2. "Load Source Image" on the right.
3. "Load Pattern Image" on the right.
4. Configure variables on the left as you wish.
5. "Generate Image"
6. Adjust variables as necessary and regenerate as necessary.
7. "Download PNG" to output finished product.
