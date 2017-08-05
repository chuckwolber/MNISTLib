# MNISTLib
A fully self contained MNIST library. This library includes the MNIST data files.

# Known Issues
If you use this as a jar file, the MNIST databases will be included as resources in that bundle. Due to jar file compression, it can take about a minute to access all of the images and labels. According to testing, it seems to take about 1/6th that time if you are not bundling this as a jar file.
