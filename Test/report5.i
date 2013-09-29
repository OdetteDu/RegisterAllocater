// COMP 412, LAB 1, Block "report5.i"
//
// a little work with powers of two
//
// Example usage: sim < report5.i
	loadI	1    => r1 
	loadI	1025 => r50 
// generate 1 to 2^16
	lshift	r1, r1  => r2 
	lshift	r2, r1  => r3 
	lshift	r3, r1  => r4 
	lshift	r4, r1  => r5 
	lshift	r5, r1  => r6 
	lshift	r6, r1  => r7 
	lshift	r7, r1  => r8 
	lshift	r8, r1  => r9 
	lshift	r9, r1  => r10 
	lshift	r10, r1 => r11 
	lshift	r11, r1 => r12 
	lshift	r12, r1 => r13 
	lshift	r13, r1 => r14 
	lshift	r14, r1 => r15 
	lshift	r15, r1 => r16 
	lshift	r16, r1 => r17 
// now sum them, spending registers to
// save adds
	add 	r1, r2   => r20 
	add	r3, r4   => r21 
	add	r5, r6   => r22 
	add	r7, r8   => r23 
	add	r9, r10  => r24 
	add	r11, r12 => r25 
	add	r13, r14 => r26 
	add	r15, r16 => r27 
	add	r20, r21 => r30 
	add	r22, r23 => r31 
	add	r24, r25 => r32 
	add	r26, r27 => r33 
	add	r30, r31 => r34 
	add	r32, r33 => r35 
	add	r35, r34 => r36 
	add	r36, r17 => r37 
// whew. Now, store the results where we can
// check them for correctness
	store	r37 => r50 
//
// sum i=1 to 16 (2^^i) should be 2^17 -1
//
	add	r5, r1   => r40 
	lshift	r1, r40  => r41 
	sub	r41, r1  => r42 
        loadI   4        => r1
	add	r50, r1  => r51 
	store	r42      => r51 
//
	output 1025
	output 1029
//
