# RIF Static Sample Files

This folder contains sample RIF files that are used in tests (and not just for this module, which is why they're in `src/main/`). See the following Java types for more information on these files:

* `gov.hhs.cms.bluebutton.datapipeline.sampledata.StaticRifResource`
* `gov.hhs.cms.bluebutton.datapipeline.sampledata.StaticRifResourceGroup`

## How to Query this Data Directly

These files are effectively just comma-separated value (CSV) files, except the field delimiter used is `|` instead of a comma. There are a number of useful Unix tools that can be used to directly analyze such files.

For analyzing a single file, it's probably simplest to use `awk`. As an example, the following command can be used to calculate the number of distinct `CLM_ID` (the 4th column) values in the `bcarrier.txt` file:

    $ awk -F '|' '{print $4}' bcarrier.txt | tail -n +2 | sort | uniq -c | wc -l

If one needs to run a query across multiple files, it's probably best to use a more sophisticated tool. I've found [the q tool](http://harelba.github.io/q/) to be quite useful: it allows you to run any SQL query against any set of CSV data. For example, I used the following query to determine that there is no single beneficiary in the `sample-b-*.txt` files that has a claim of every type (only took 1.3s to run):

    $ q --skip-header --delimiter="|" \
        "select COUNT(DISTINCT(benes.BENE_ID)) \
          from ./sample-b-beneficiaries-1000.txt AS benes \
            INNER JOIN sample-b-bcarrier-1091.txt AS bcarrier ON benes.BENE_ID=bcarrier.BENE_ID \
            INNER JOIN ./sample-b-pde-1195.txt AS pde ON benes.BENE_ID=pde.BENE_ID \
            INNER JOIN ./sample-b-dme.txt AS dme ON benes.BENE_ID=dme.BENE_ID \
            INNER JOIN ./sample-b-hha.txt AS hha ON benes.BENE_ID=hha.BENE_ID \
            INNER JOIN ./sample-b-hospice.txt AS hospice ON benes.BENE_ID=hospice.BENE_ID \
            INNER JOIN ./sample-b-inpatient.txt AS inpatient ON benes.BENE_ID=inpatient.BENE_ID \
            INNER JOIN ./sample-b-outpatient.txt AS outpatient ON benes.BENE_ID=outpatient.BENE_ID \
            INNER JOIN ./sample-b-snf.txt AS snf ON benes.BENE_ID=snf.BENE_ID"
