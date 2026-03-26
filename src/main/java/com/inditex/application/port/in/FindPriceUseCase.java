package com.inditex.application.port.in;

import com.inditex.application.dto.PriceQuery;
import com.inditex.application.dto.PriceResponse;

public interface FindPriceUseCase {

    PriceResponse execute(PriceQuery priceQuery);

}
