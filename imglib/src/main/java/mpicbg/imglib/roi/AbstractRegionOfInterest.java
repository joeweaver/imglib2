package mpicbg.imglib.roi;

import java.util.Arrays;
import java.util.Iterator;

import mpicbg.imglib.Cursor;
import mpicbg.imglib.IterableInterval;
import mpicbg.imglib.IterableRealInterval;
import mpicbg.imglib.Localizable;
import mpicbg.imglib.RandomAccess;
import mpicbg.imglib.RandomAccessible;
import mpicbg.imglib.RealLocalizable;
import mpicbg.imglib.RealRandomAccess;
import mpicbg.imglib.type.Type;
import mpicbg.imglib.type.logic.BitType;

/**
 * @author leek
 *
 *The AbstractRegionOfInterest implements the IterableRegionOfInterest
 *using a raster function and a membership function that are
 *implemented by a derived class.
 */
public abstract class AbstractRegionOfInterest implements IterableRegionOfInterest {
	private int nDimensions;
	static final private long SIZE_NOT_CACHED = -1;
	private long cached_size = SIZE_NOT_CACHED;
	private double [] cached_real_min;
	private double [] cached_real_max;
	private long [] cached_min;
	private long [] cached_max;
	/**
	 * @author leek
	 *The AROIRandomAccess inner class implements the random access part of the
	 *ROI, allowing random sampling of pixel membership in the ROI.
	 */
	protected class AROIRandomAccess implements RealRandomAccess<BitType> {

		private BitType bit_type = new BitType();
		private double [] position;
		public AROIRandomAccess() {
			position = new double[nDimensions];
		}
		@Override
		public void localize(float[] position) {
			for (int i = 0; i < position.length; i++) {
				position[i] = (float)this.position[i];
			}
		}

		@Override
		public void localize(double[] position) {
			for (int i = 0; i < position.length; i++) {
				position[i] = this.position[i];
			}
		}

		@Override
		public float getFloatPosition(int dim) {
			// TODO Auto-generated method stub
			return (float)position[dim];
		}

		@Override
		public double getDoublePosition(int dim) {
			// TODO Auto-generated method stub
			return position[dim];
		}

		@Override
		public int numDimensions() {
			// TODO Auto-generated method stub
			return nDimensions;
		}

		@Override
		public void move(float distance, int dim) {
			position[dim] += distance;
			updateCachedMembershipStatus();
		}

		@Override
		public void move(double distance, int dim) {
			position[dim] += distance;
			updateCachedMembershipStatus();
		}

		@Override
		public void move(int distance, int dim) {
			position[dim] += distance;
			updateCachedMembershipStatus();
		}

		@Override
		public void move(long distance, int dim) {
			position[dim] += distance;
			updateCachedMembershipStatus();
		}

		@Override
		public void move(RealLocalizable localizable) {
			for (int i = 0; i < position.length; i++) {
				position[i] += localizable.getDoublePosition(i);
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void move(Localizable localizable) {
			for (int i = 0; i < position.length; i++) {
				position[i] += localizable.getDoublePosition(i);
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void move(float[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] += position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void move(double[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] += position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void move(int[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] += position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void move(long[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] += position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(RealLocalizable localizable) {
			localizable.localize(position);
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(Localizable localizable) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] = localizable.getDoublePosition(i);
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(float[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] = position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(double[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] = position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(int[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] = position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(long[] position) {
			for (int i = 0; i < position.length; i++) {
				this.position[i] = position[i];
			}
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(float position, int dim) {
			this.position[dim] = position;
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(double position, int dim) {
			this.position[dim] = position;
			updateCachedMembershipStatus();
		}


		@Override
		public void setPosition(int position, int dim) {
			this.position[dim] = position;
			updateCachedMembershipStatus();
		}

		@Override
		public void setPosition(long position, int dim) {
			this.position[dim] = position;
			updateCachedMembershipStatus();
		}

		protected void updateCachedMembershipStatus() {
			bit_type.set(isMember(position));
		}
		
		@Override
		public void fwd(int dim) {
			position[dim] += 1;
			updateCachedMembershipStatus();
		}

		@Override
		public void bck(int dim) {
			position[dim] -= 1;
			updateCachedMembershipStatus();
		}

		@Override
		public BitType get() {
			return bit_type;
		}

		@Override
		public BitType getType() {
			return new BitType();
		}
		
	}
	
	protected class AROIIterableInterval <T extends Type<T>> implements IterableInterval<T> {
		protected RandomAccessible<T> src;
		protected T cached_first_element;
		
		private AbstractRegionOfInterest getEnclosingClass() {
			return AbstractRegionOfInterest.this;
		}
		public AROIIterableInterval(RandomAccessible<T> src) {
			this.src = src;
		}
		
		protected class AROICursor implements Cursor<T> {
			private RandomAccess<T> src = AROIIterableInterval.this.src.randomAccess();
			private long [] position = new long [AbstractRegionOfInterest.this.numDimensions()];
			private long [] next_position = new long [AbstractRegionOfInterest.this.numDimensions()];
			private long [] raster_end = new long [AbstractRegionOfInterest.this.numDimensions()];			
			private long [] next_raster_end = new long [AbstractRegionOfInterest.this.numDimensions()];			
			private boolean next_is_valid = false;
			private boolean has_next;
			private boolean src_is_valid = false;
			
			private void mark_dirty() {
				next_is_valid = false;
				src_is_valid = false;
			}

			public AROICursor() {
				reset();
			}
			@Override
			public void localize(float[] position) {
				for ( int d = 0; d < position.length; d++ )
					position[ d ] = (float)this.position[ d ];
			}

			@Override
			public void localize(double[] position) {
				for ( int d = 0; d < position.length; d++ )
					position[ d ] = this.position[ d ];
			}

			@Override
			public void localize(int[] position) {
				for ( int d = 0; d < position.length; d++ )
					position[ d ] = (int)this.position[ d ];
			
			}

			@Override
			public void localize(long[] position) {
				for ( int d = 0; d < position.length; d++ )
					position[ d ] = this.position[ d ];
			}

			@Override
			public float getFloatPosition(int dim) {
				return (float)position[dim];
			}

			@Override
			public double getDoublePosition(int dim) {
				return position[dim];
			}

			@Override
			public int getIntPosition(int dim) {
				// TODO Auto-generated method stub
				return (int)position[dim];
			}

			@Override
			public long getLongPosition(int dim) {
				// TODO Auto-generated method stub
				return position[dim];
			}
			
			@Override
			public int numDimensions() {
				return AbstractRegionOfInterest.this.numDimensions();
			}

			@Override
			public T get() {
				if (! src_is_valid) {
					src.setPosition(position);
				}
				return src.get();
			}

			@SuppressWarnings("deprecation")
			@Override
			public T getType() {
				// TODO Auto-generated method stub
				return src.getType();
			}

			@Override
			public void jumpFwd(long steps) {
				if (! AbstractRegionOfInterest.this.jumpFwd(steps, position, raster_end)) {
					throw new IllegalAccessError("Jumped past end of sequence");
				}
				mark_dirty();
			}

			@Override
			public void fwd() {
				if (! hasNext()) {
					throw new IllegalAccessError("fwd called at end of sequence");
				}
				for (int i = 0; i < position.length; i++) {
					position[i] = next_position[i];
					raster_end[i] = next_raster_end[i];
				}
				mark_dirty();
			}

			@Override
			public void reset() {
				for (long [] a : new long [][] { position, next_position, raster_end, next_raster_end }) {
					Arrays.fill(a, Long.MIN_VALUE);
				}
				next_raster_end[0]++;
				mark_dirty();
			}

			@Override
			public boolean hasNext() {
				if (! next_is_valid) {
					has_next = AbstractRegionOfInterest.this.jumpFwd(1, next_position, next_raster_end);
				}
				next_is_valid = true;
				return has_next;
			}

			@Override
			public T next() {
				fwd();
				return get();
			}

			@Override
			public void remove() {
				AbstractRegionOfInterest.this.remove(position);
				mark_dirty();
			}

		}
		@Override
		public long size() {
			return getCachedSize();
		}

		@Override
		public T firstElement() {
			if (cached_first_element == null){
				RandomAccess<T> r = src.randomAccess();
				long [] position = new long [numDimensions()];
				long [] raster_end = new long [numDimensions()];
				Arrays.fill(position, Long.MIN_VALUE);
				Arrays.fill(raster_end, Long.MIN_VALUE);
				if (! nextRaster(position, raster_end)) {
					throw new IllegalAccessError("Tried to get first element, but ROI has no elements");
				}
				r.setPosition(position);
				cached_first_element = r.get();
			}
			return cached_first_element;
		}

		@Override
		public boolean equalIterationOrder(IterableRealInterval<?> f) {
			if (f instanceof AROIIterableInterval) {
				@SuppressWarnings("unchecked")
				AROIIterableInterval<T> af = ((AROIIterableInterval<T>) f);
				return af.getEnclosingClass() == getEnclosingClass();
			}
			return false;
		}

		@Override
		public double realMin(int d) {
			return AbstractRegionOfInterest.this.realMin(d);
		}

		@Override
		public void realMin(double[] min) {
			AbstractRegionOfInterest.this.realMin(min);
		}

		@Override
		public double realMax(int d) {
			return AbstractRegionOfInterest.this.realMax(d);
		}

		@Override
		public void realMax(double[] max) {
			AbstractRegionOfInterest.this.realMax(max);
		}

		@Override
		public int numDimensions() {
			// TODO Auto-generated method stub
			return AbstractRegionOfInterest.this.numDimensions();
		}

		@Override
		public Iterator<T> iterator() {
			// TODO Auto-generated method stub
			return new AROICursor();
		}

		@Override
		public long min(int d) {
			return AbstractRegionOfInterest.this.min(d);
		}

		@Override
		public void min(long[] min) {
			AbstractRegionOfInterest.this.min(min);
		}

		@Override
		public long max(int d) {
			return AbstractRegionOfInterest.this.max(d);
		}

		@Override
		public void max(long[] max) {
			AbstractRegionOfInterest.this.max(max);
		}

		@Override
		public void dimensions(long[] dimensions) {
			AbstractRegionOfInterest.this.dimensions(dimensions);
		}

		@Override
		public long dimension(int d) {
			// TODO Auto-generated method stub
			return AbstractRegionOfInterest.this.dimension(d);
		}

		@Override
		public Cursor<T> cursor() {
			// TODO Auto-generated method stub
			return new AROICursor();
		}

		@Override
		public Cursor<T> localizingCursor() {
			// TODO Auto-generated method stub
			return new AROICursor();
		}
		
	}
	
	protected AbstractRegionOfInterest(int nDimensions) {
		this.nDimensions = nDimensions;
	}
	
	/**
	 * Determine whether a point is a member of the region of interest
	 * @param position position in question
	 * @return true if a member
	 */
	abstract protected boolean isMember(double [] position);
	
	/**
	 * Advance the position to the next raster.
	 * 
	 * The AbstractRegionOfInterest successively adds one to the
	 * lowest dimension of the position until the position is outside
	 * the ROI as determined by isMember. At this point, it calls
	 * nextRaster to find the start of the next raster.
	 * 
	 * As an example, a rectangle might have x, y, width and height.
	 * The code would determine if the y position was before, within or after
	 * the rectangle bounds. If before, set position[0] = x, position[1] = y
	 * and return true, if within, set position[0] = x, position[1] += 1,
	 * if after, return false.
	 * 
	 * @param position on entry, the position of the raster after advancement
	 *                 to its end (or initial or final position)
	 * @param end on exit, the coordinates of the end of the raster. Index 0
	 *        is generally the only pertinent variable, subsequent indices should
	 *        be duplicates of the start raster. Nevertheless, using an array
	 *        lets the caller pass the results as a modification of the array.
	 * @return true if there is a raster after this one.
	 */
	protected abstract boolean nextRaster(long [] position, long [] end);
	
	/**
	 * Jump forward a certain number of steps from the given position.
	 * 
	 * The implementer may want to override this function. For instance,
	 * for a rectangle, the implementer may want to advance the position
	 * by the number of steps if the number of steps is less than x - width
	 * or perform a more complicated operation involving division by the
	 * width if the number of steps is greater.
	 * 
	 * @param steps - number of steps to move
	 * @param position - the internal position which should be advanced by the number of steps
	 * @param end - the end position of the current raster on entry and on exit.
	 * @return true if taking that number of steps still lands within the ROI.
	 */
	protected boolean jumpFwd(long steps, long [] position, long [] end) {
		while(true) {
			if (position[0] + steps < end[0]) {
				position[0] += steps;
				return true;
			}
			steps -= end[0] - position[0];
			position[0] = end[0];
			if (! nextRaster(position, end)) return false;
		}  
	}
	
	/**
	 * Return the # of elements available from a cursor over the ROI.
	 * The default method acquires successive rasters using nextRaster
	 * to get a sum of pixels. The implementer should consider overriding
	 * this to provide a more efficient implementation.
	 * @return
	 */
	protected long size() {
		long [] position = new long [nDimensions];
		long [] end = new long [nDimensions];
		Arrays.fill(position, Long.MIN_VALUE);
		long accumulator = 0;
		while (nextRaster(position, end)) {
			accumulator += end[0] - position[0];
			position[0] = end[0];
		}
		return accumulator;
	}
	
	/**
	 * Get the minimum and maximum corners of a bounding
	 * hypercube around all points in the ROI.
	 * 
	 * The implementer should strongly consider implementing this
	 * to provide a more efficient implementation.
	 * 
	 * @param minima - minimum coordinates of the ROI
	 * @param maxima - maximum coordinates of the ROI
	 */
	protected void getExtrema(long [] minima, long [] maxima) {
		long [] position = new long [nDimensions];
		long [] end = new long [nDimensions];
		Arrays.fill(position, Long.MIN_VALUE);
		Arrays.fill(minima, Long.MAX_VALUE);
		Arrays.fill(maxima, Long.MIN_VALUE);
		while (nextRaster(position, end)) {
			for (int i = 0; i < position.length; i++) {
				minima[i] = Math.min(minima[i], position[i]);
				if (i == 0) {
					// 0 - the end has the maximum position, non-inclusive
					maxima[i] = Math.max(maxima[i], end[i] - 1);
				} else {
					maxima[i] = Math.max(maxima[i], position[i]);
				}
			}
			position[0] = end[0];
		}
	}
	
	/**
	 * Get the minimum and maximum corners of a bounding hypercube
	 * using real coordinates (which might have fractional components)
	 * 
	 * The implementer should only override this if the ROI is
	 * described in real coordinates. Otherwise, the pixel extrema are used.
	 * 
	 * @param minima
	 * @param maxima
	 */
	protected void getRealExtrema(double [] minima, double [] maxima) {
		validateExtremaCache();
		for (int i = 0; i < numDimensions(); i++) {
			minima[i] = cached_min[i];
			maxima[i] = cached_max[i];
		}
	}
	
	/**
	 * Remove a pixel from a ROI if possible.
	 * 
	 * The implementer can override this to add a removal behavior
	 * to their ROI, for instance, turning off a mask bit at the
	 * indicated position.
	 * @param position - position that should be removed from the ROI.
	 */
	protected void remove(final long [] position) {
	}
	
	private void validateExtremaCache() {
		if (cached_max == null) {
			long [] cached_min = new long [nDimensions];
			long [] cached_max = new long [nDimensions];
			getExtrema(cached_min, cached_max);
			this.cached_min = cached_min;
			this.cached_max = cached_max;
		}
	}
	
	private void validateRealExtremaCache() {
		if (cached_real_min == null) {
			double [] cached_real_min = new double [nDimensions];
			double [] cached_real_max = new double [nDimensions];
			getRealExtrema(cached_real_min, cached_real_max);
			this.cached_real_min = cached_real_min;
			this.cached_real_max = cached_real_max;
		}
	}
	protected long dimension(int d) {
		validateExtremaCache();
		return cached_max[d] - cached_min[d] + 1;
	}

	protected void dimensions(long[] d) {
		for (int i = 0; i < d.length; i++) {
			d[i] = dimension(i);
		}
	}

	protected void max(long[] max) {
		validateExtremaCache();
		for (int i = 0; i < max.length; i++) {
			max[i] = cached_max[i];
		}
	}

	protected long max(int d) {
		validateExtremaCache();
		return cached_max[d];
	}

	protected void min(long[] min) {
		validateExtremaCache();
		for (int i = 0; i < min.length; i++) {
			min[i] = cached_min[i];
		}
		
	}

	protected long min(int d) {
		validateExtremaCache();
		return cached_min[d];
	}

	protected double realMin(int d) {
		validateRealExtremaCache();
		return cached_real_min[d];
	}

	protected void realMin(double[] min) {
		validateRealExtremaCache();
		for (int i = 0; i < min.length; i++) {
			min[i] = cached_real_min[i];
		}
	}

	protected double realMax(int d) {
		validateRealExtremaCache();
		return cached_real_max[d];
	}

	protected void realMax(double[] max) {
		validateRealExtremaCache();
		for (int i = 0; i < max.length; i++) {
			max[i] = cached_real_max[i];
		}
	}

	protected long getCachedSize() {
		if (cached_size == SIZE_NOT_CACHED) {
			cached_size = size();
		}
		return cached_size;
	}

	@Override
	public int numDimensions() {
		return nDimensions;
	}

	@Override
	public RealRandomAccess<BitType> realRandomAccess() {
		return new AROIRandomAccess();
	}

	@Override
	public <T extends Type<T>> IterableInterval<T> getIterableIntervalOverROI(
			RandomAccessible<T> src) {
		return new AROIIterableInterval<T>(src);
	}
}
