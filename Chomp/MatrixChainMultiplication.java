package packagename;
import java.util.Calendar;
import java.util.Random;
public class MatrixChainMultiplication {
	// Dynamic Programming Python implementation of Matrix Chain Multiplication. 

	// Matrix Ai has dimension p[i-1] x p[i] for i = 1..n 
	private static int MatrixChainOrderDP(int p[], int n) 
	{ 
		/* For simplicity of the program, one extra row and one 
	        extra column are allocated in m[][].  0th row and 0th 
	        column of m[][] are not used */
		int m[][] = new int[n][n]; 

		int i, j, k, L, q; 

		/* m[i,j] = Minimum number of scalar multiplications needed 
	        to compute the matrix A[i]A[i+1]...A[j] = A[i..j] where 
	        dimension of A[i] is p[i-1] x p[i] */

		// cost is zero when multiplying one matrix. 
		for (i = 1; i < n; i++) 
			m[i][i] = 0; 

		// L is chain length. 
		for (L=2; L<n; L++) 
		{ 
			for (i=1; i<n-L+1; i++) 
			{ 
				j = i+L-1; 
				if(j == n) continue; 
				m[i][j] = Integer.MAX_VALUE; 
				for (k=i; k<=j-1; k++) 
				{ 
					// q = cost/scalar multiplications 
					q = m[i][k] + m[k+1][j] + p[i-1]*p[k]*p[j]; 
					if (q < m[i][j]) 
						m[i][j] = q; 
				} 
			} 
		} 

		return m[1][n-1]; 
	} 

	// Matrix Ai has dimension p[i-1] x p[i] for i = 1..n 
	private static int MatrixChainOrder(int p[], int i, int j) 
	{ 
		if (i == j) 
			return 0; 

		int min = Integer.MAX_VALUE; 

		// place parenthesis at different places between first 
		// and last matrix, recursively calculate count of 
		// multiplications for each parenthesis placement and 
		// return the minimum count 
		for (int k=i; k<j; k++) 
		{ 
			int count = MatrixChainOrder(p, i, k) + 
					MatrixChainOrder(p, k+1, j) + 
					p[i-1]*p[k]*p[j]; 

			if (count < min) 
				min = count; 
		} 

		// Return minimum count 
		return min; 
	} 
	// Driver program to test above function 
	public static void main(String args[]) 
	{ 
		// creating random numbered arrays
		Random rand = new Random();
		int arr [] = new int[25];
		int n = arr.length;

		for(int i=0;i<n;i++) {
			arr[i] = rand.nextInt(11); 
		}

		Calendar a1 = Calendar.getInstance();
		long t1 = a1.getTimeInMillis();
		System.out.println("Minimum number of multiplications is "+ MatrixChainOrderDP(arr, n)); 
		Calendar a2 = Calendar.getInstance();
		System.out.println("DP(iterative) Approach : "+(a2.getTimeInMillis()-t1)/1000.0+" sec ");

		Calendar b1 = Calendar.getInstance();
		long t2 = b1.getTimeInMillis();
		System.out.println("Minimum number of multiplications is "+ MatrixChainOrder(arr, 1, n-1)); 
		Calendar b2 = Calendar.getInstance();
		System.out.println("Recusive Approach : "+(b2.getTimeInMillis()-t2)/1000.0+" sec ");

	} 
} 

