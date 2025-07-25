Pixels
=======

The core of Pixels is a columnar storage engine designed for data lakes and warehouses.
It is optimized for analytical tables stored in on-premises and cloud-native storage systems,
including S3, GCS, HDFS, Redis, and local file systems.
Pixels outperforms Parquet, which is the most widely used columnar format in today's lakehouses, by up to two orders of magnitude.

We have integrated Pixels with popular query engines including DuckDB (1.1.0), Trino (405 and 466), StarRocks (3.3.5), PrestoDB (0.279), and Hive (2.3+).

The DuckDB integration and the C++ implementation of Pixels Reader are in the [cpp](cpp) folder.
The other integrations are opensourced in separate repositories:
* [Pixels Connector for Trino](https://github.com/pixelsdb/pixels-trino)
* [Pixels Connector for PrestoDB](https://github.com/pixelsdb/pixels-presto)
* [StarRocks with Pixels Integration](https://github.com/pixelsdb/starrocks)
* [Pixels SerDe for Hive](https://github.com/pixelsdb/pixels-hive)

Pixels also has its own query engine [Pixels-Turbo](pixels-turbo).
It prioritizes processing queries in an autoscaling MPP cluster (currently based on Trino) and exploits serverless functions 
(e.g, [AWS Lambda](https://aws.amazon.com/lambda/) or [vHive / Knative](https://github.com/vhive-serverless/vHive)) 
to accelerate the processing of workload spikes. With `Pixels-Turbo`, we can achieve better performance and cost-efficiency 
for continuous workloads while not compromising elasticity for workload spikes.

Based on Pixels-Turbo, we implement [Pixels-Rover](https://github.com/pixelsdb/pixels-rover), a web-based query interface
that provides users with a complete experience of serverless query processing, natural-language-to-SQL translation, and flexible
service levels in query urgency. It allows users to select whether to execute the query immediately, within a grace period, or eventually.
Pixels-Turbo can apply different resource scheduling and query execution policies for Different levels of query urgency, which
will result in different monetary costs on resources.

## Build Pixels

Pixels is mainly implemented in both Java (with some JNI hooks of system calls and C/C++ libs) and C++.
The [C++ document](cpp/README.md) provides the instructions to build and run the C++ codebase. Here we explain how to build and use the Java components.

JDK 8 (or above) and Maven 3.8 (or above) are required to build Pixels.
Earlier Maven versions may work but are not tested.
After installing these prerequisites, enter any `SRC_BASE` directory, clone the Pixels codebase and build it as follows:
```bash
git clone https://github.com/pixelsdb/pixels.git
cd pixels
# ensure PIXELS_HOME environment variable is set to the installation directory of pixels (not SRC_BASE).
export PIXELS_HOME=[pixels-install-dir]
mvn clean install
```

It may take a couple of minutes to complete. After that, the library jars of Pixels has been installed to the local Maven repository.
Please also find the executable jar files of Pixels:
* `pixels-daemon-*-full.jar` in `pixels-daemon/target`,this is the jar to run Pixels daemons.
* `pixels-cli-*-full.jar` in `pixels-cli/target`, this is the jar of Pixels command line tool.

They will be used in the installation of Pixels.

> Note: Some Junit tests in Pixels access some low-level packages in the JDK, such as sun.nio and java.nio.
> Compiling and running such test cases require lower version JDKs (e.g., 1.8). However, these tests are not necessary for the aforementioned build process.

Pixels is compatible with different query engines, such as Trino, Presto, and Hive.
The query engine integrations also can be built using maven.
For example, to build the Trino integration for Pixels, just git clone [pixels-trino](https://github.com/pixelsdb/pixels-trino), 
and build it using `mvn package` in the local git repository.

> Pixels by itself is compatible with Java 8+ and Maven 3.8+. However, third-party query engines such as Trino may require
> a later JDK (e.g., Trino 405/466 requires JDK17.0.3+/23.0.0+) and Maven.
> It is fine to build the query engine integration (e.g., `pixels-trino`) with the same or higher versions of JDK and Maven than Pixels.


## Develop Pixels in IntelliJ

If you want to develop Pixels in Intellij, open `SRC_BASE/pixels` as a maven project.
When the project is fully indexed and the dependencies are successfully downloaded, 
you can build Pixels using the maven plugin (as an alternative of `mvn package`), run and debug unit tests, and debug Pixels by
setting up a *Remote JVM Debug*. 
Ensure the environment variable `PIXELS_HOME` is set to the installation directory of Pixels for the maven plugin and the run/debug targets in IntelliJ.

In some versions of IntelliJ, the default `idea.max.intellisense.filesize` in IntelliJ may be not large enough for the source files generated by ProtoBuf.
Hence, the large generated source file will be considered as plain text file in the user interface.
To solve this problem, set this property to `4096` (i.e., 4MB) or larger in `Help` -> `Edit Custom Properties...` and restart Intellij.

> To use the maven plugin, run/debug the unit tests, or run/debug the main classes of Pixels in Intellij, set the `PIXELS_HOME` environment
> variable for `Maven`, `Junit`, or `Application` in `Run` -> `Edit Configurations` -> `Edit Configuration Templetes`.
> Ensure that the `PIXELS_HOME` directory exists and follow the instructions in [Install Pixels](docs/INSTALL.md#install-pixels) to put
> the `pixels.properties` into `PIXELS_HOME/etc` and create the `logs` directory where the log files will be
> written into.


## Deploy and Evaluate Pixels

You can follow the [Installation](docs/INSTALL.md) instructions to deploy Pixels in a cluster,
and learn how to use Pixels and evaluate its performance following [TPC-H Evaluation](docs/TPC-H.md) or [ClickBench Evaluation](docs/CLICKBENCH.md).


## Contributing

We welcome contributions to Pixels and its subprojects. If you are interested in contributing to Pixels, 
please read our [Git Workflow](https://github.com/pixelsdb/pixels/wiki/Git-Workflow).


## Publications

Pixels is an academic system aims at providing production-grade quality. It supports all the functionalities required by TPC-H and
is compatible with the mainstream data analytic ecosystems.
The key ideas and insights in Pixels are elaborated in the following publications.

> `ICDE'25` [PixelsDB: Serverless and NL-Aided Data Analytics with Flexible Service Levels and Prices](https://arxiv.org/abs/2405.19784)\
> Haoqiong Bian, Dongyang Geng, Haoyang Li, Yunpeng Chai, Anastasia Ailamaki

> `arXiv'24` [Serverless Query Processing with Flexible Performance SLAs and Prices](https://arxiv.org/abs/2409.01388)\
> Haoqiong Bian, Dongyang Geng, Yunpeng Chai, Anastasia Ailamaki

> `SIGMOD'23` [Using Cloud Functions as Accelerator for Elastic Data Analytics](https://doi.org/10.1145/3589306)\
> Haoqiong Bian, Tiannan Sha, Anastasia Ailamaki

> `EDBT'22` [Columnar Storage Optimization and Caching for Data Lakes (short)](https://doi.org/10.48786/edbt.2022.33)\
> Guodong Jin, Haoqiong Bian, Yueguo Chen, Xiaoyong Du

> `ICDE'22` [Pixels: An Efficient Column Store for Cloud Data Lakes](https://doi.org/10.1109/ICDE53745.2022.00276)\
> Haoqiong Bian, Anastasia Ailamaki

> `CIDR'20` [Pixels: Multiversion Wide Table Store for Data Lakes (abstract)](https://www.cidrdb.org/cidr2020/gongshow2020/gongshow/abstracts/cidr2020_abstract74.pdf)\
> Haoqiong Bian

> `ICDE'18` [Rainbow: Adaptive Layout Optimization for Wide Tables (demo)](https://doi.org/10.1109/ICDE.2018.00200)\
> Haoqiong Bian, Youxian Tao, Guodong Jin, Yueguo Chen, Xiongpai Qin, Xiaoyong Du

> `SIGMOD'17` [Wide Table Layout Optimization by Column Ordering and Duplication](https://doi.org/10.1145/3035918.3035930)\
> Haoqiong Bian, Ying Yan, Wenbo Tao, Liang Jeff Chen, Yueguo Chen, Xiaoyong Du, Thomas Moscibroda
