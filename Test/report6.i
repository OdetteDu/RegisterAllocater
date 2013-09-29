// COMP 412, LAB 1, Block "report6.i"
//
// example 5, worked with fewer registers 
//
// Example usage: sim < report6.i
//
	loadI	0    => r0 
	loadI	1    => r1 
	loadI	1025 => r50 
// generate 1 to 2^16
	add	r0, r1	=> r0 
	lshift	r1, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
	lshift	r2, r1  => r2 
	add	r0, r2  => r0 
// whew. Now, store the results where we can
// check them for correctness
	store	r0  => r50 
//
// sum i=0 to 16 (2^^i) should be 2^17 -1
//
// since we didn't save the results, this
// part is a little harder, but still easy
	loadI	17      => r17 
	lshift  r1, r17 => r18 
	sub	r18, r1 => r19
        loadI   4       => r1 
	add	r50, r1 => r51 
	store	r19     => r51 
//
	output	1025
	output 	1029
//
