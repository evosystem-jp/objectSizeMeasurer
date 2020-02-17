# Measure size of objects in an image using JavaCV

The project provides a script to read an image and draw objects contours.

### Constraints
1. Shadow effect: use dark background
2. Object boundry: use contrasting background

## Getting Started

### Run

#### For image file
```sh
java jp.evosystem.objectSizeMeasure.ObjectSize
```

#### For web camera image
```sh
java jp.evosystem.objectSizeMeasure.WebCameraObjectSize
```

## Algorithm
1. Image pre-processing
  - Read an image and convert it it no grayscale
  - Blur the image using Gaussian Kernel to remove un-necessary edges
  - Edge detection using Canny edge detector
  - Perform morphological closing operation to remove noisy contours

2. Object Segmentation
  - Find contours
  - Remove small contours by calculating its area (threshold used here is 100)
  - Sort contours from left to right to find the reference objects

3. Compute results
  - Draw bounding boxes around each object and calculate its height and width

## Results

![Result](images/result.jpg?raw=true "Title")

## Authors

* **Evosystem, inc.**

## Acknowledgments

* https://www.pyimagesearch.com/
* https://github.com/snsharma1311/object-size