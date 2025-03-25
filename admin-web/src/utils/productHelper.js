/**
 * 生成标准化的商品参数对象
 * @param {Object} productParam 原始商品参数
 * @returns {Object} 标准化的商品参数
 */
export function createStandardProductParam(productParam) {
  // 确保所有必要字段都有默认值
  return {
    // 商品基本信息对象
    productParam: {
      name: productParam.name,
      productSn: productParam.productSn || generateProductSn(),
      brandId: Number(productParam.brandId) || 0,
      merchantId: productParam.merchantId || 1,
      categoryId: Number(productParam.productCategoryId) || 0,
      attributeId: 0, // 必须设置默认值
      price: productParam.price || 0,
      originalPrice: productParam.originalPrice || 0,
      stock: productParam.stock || 0,
      unit: productParam.unit || '件',
      weight: productParam.weight || 0,
      sort: productParam.sort || 0,
      keywords: productParam.keywords || '',
      albumPics: productParam.albumPics || '',
      pic: productParam.pic || '',
      publishStatus: productParam.publishStatus || 0,
      newStatus: productParam.newStatus || 0,
      recommendStatus: productParam.recommendStatus || 0,
      sale: 0,
      promotionType: 0
    },
    // 商品属性值列表
    productAttributeValueList: productParam.productAttributeValueList || [],
    // 商品满减价格列表
    productFullReductionList: productParam.productFullReductionList || [],
    // 商品阶梯价格列表
    productLadderList: productParam.productLadderList || [],
    // 商品SKU库存列表
    skuStockList: productParam.skuStockList || []
  };
}

/**
 * 生成商品编号
 * @returns {string} 生成的商品编号
 */
function generateProductSn() {
  return 'P' + Date.now();
} 