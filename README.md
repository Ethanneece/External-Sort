# External sort for large files

When it is impossible to sort all the data in memory
    this type of sort is implented. The data is read 
    in blocks and then outputted to a file until it 
    is fully sorted, than it uses merge sort to 
    read all the sorted chunks of data until the 
    