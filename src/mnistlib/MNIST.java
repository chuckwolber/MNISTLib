/**
 * Copyright 2017 Chuck Wolber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mnistlib;

import idx.IDX;
import idx.IDXException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author chuckwolber
 */
public class MNIST
{
    private static final String TRAINING_LABELS = "resources/train-labels-idx1-ubyte";
    private static final String TRAINING_IMAGES = "resources/train-images-idx3-ubyte";
    private static final String TESTING_LABELS = "resources/t10k-labels-idx1-ubyte";
    private static final String TESTING_IMAGES = "resources/t10k-images-idx3-ubyte";
    
    private final InputStream _trLabelStream = MNIST.class.getClassLoader().getResourceAsStream(TRAINING_LABELS);
    private final InputStream _trImageStream = MNIST.class.getClassLoader().getResourceAsStream(TRAINING_IMAGES);
    private final InputStream _teLabelStream = MNIST.class.getClassLoader().getResourceAsStream(TESTING_LABELS);
    private final InputStream _teImageStream = MNIST.class.getClassLoader().getResourceAsStream(TESTING_IMAGES);
    
    private IDX _idxTrainingLabels;
    private IDX _idxTrainingImages;
    private IDX _idxTestingLabels;
    private IDX _idxTestingImages;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try {
            MNIST m = MNIST.getInstance();
            System.out.println("Training -> " + m.numTrainingImages());
            for (long i=0; i<m.numTrainingImages(); i++) {
                m.getNextTrainingLabel();
                m.getNextTrainingImage();
            }
            System.out.println("Testing -> " + m.numTestingImages());
            for (long i=0; i<m.numTestingImages(); i++) {
                m.getNextTestingLabel();
                m.getNextTestingImage();
            }
            System.out.println("Done");
        } catch (IOException | IDXException e) {
            System.out.println(e.toString());  
        }
    }
    
    public static MNIST getInstance() throws IOException, IDXException {
        MNIST m = new MNIST();
        m._idxTrainingLabels = IDX.getInstance(m._trLabelStream);
        m._idxTrainingImages = IDX.getInstance(m._trImageStream);
        m._idxTestingLabels = IDX.getInstance(m._teLabelStream);
        m._idxTestingImages = IDX.getInstance(m._teImageStream);
        return m;
    }
    
    public Long numTrainingImages() {
        return _idxTrainingImages.numObjects();
    }
    
    public Long numTestingImages() {
        return _idxTestingImages.numObjects();
    }
    
    public MImage getNextTrainingImage() {
        return getNextImageObject(_idxTrainingImages);
    }
    
    public Integer getNextTrainingLabel() {
        return getNextLabelObject(_idxTrainingLabels);
    }
    
    public MImage getNextTestingImage() {
        return getNextImageObject(_idxTestingImages);
    }
    
    public Integer getNextTestingLabel() {
        return getNextLabelObject(_idxTestingLabels);
    }
    
    private Integer imageRows() {
        ArrayList<Integer> dimensions = _idxTrainingImages.dimensionList();
        if (dimensions.size() > 1)
            return dimensions.get(0);
        return 0;
    }
    
    private Integer imageColumns() {
        ArrayList<Integer> dimensions = _idxTrainingImages.dimensionList();
        if (dimensions.size() >= 2)
            return dimensions.get(1);
        return 0;
    }
    
    private MImage getNextImageObject(IDX idx) {
        return new MImage(getNextObject(idx), imageRows(), imageColumns());
    }
    
    private Integer getNextLabelObject(IDX idx) {
        ArrayList<Integer> object = getNextObject(idx);
        if (object.size() > 0)
            return object.get(0);
        return null;
    }
    
    private ArrayList<Integer> getNextObject(IDX idx) {
        ArrayList<Integer> object = null;
        try {
            object = idx.getNextObject();
        } catch (IOException | IDXException e) {
            System.out.println(e.toString());
        }
        return object;
    }
}
