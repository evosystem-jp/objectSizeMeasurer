# Measure size of objects in an image using JavaCV

The project provides a script to read an image and draw objects contours.

### Constraints
1. Shadow effect: use dark background
2. Object boundry: use contrasting background

## Getting Started

### Prerequisites
1. Java 8 (maybe or higher)
2. Maven

### Installing
1. Clone repository
 - `git clone https://github.com/evosystem-jp/objectSizeMeasure.git`
2. Install dependencies
 - `mvn install`

### Run

#### For image file
```sh
mvn exec:java -Dexec.mainClass="jp.evosystem.objectSizeMeasure.mains.ImageFileObjectSize"
```

#### For video file
```sh
mvn exec:java -Dexec.mainClass="jp.evosystem.objectSizeMeasure.mains.VideoFileObjectSize"
```

#### For web camera image
```sh
mvn exec:java -Dexec.mainClass="jp.evosystem.objectSizeMeasure.mains.WebCameraObjectSize"
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

* https://www.pyimagesearch.com/2016/03/28/measuring-size-of-objects-in-an-image-with-opencv/
* https://github.com/snsharma1311/object-size